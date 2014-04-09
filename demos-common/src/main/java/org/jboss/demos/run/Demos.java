/**
 * 
 */
package org.jboss.demos.run;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;
import org.jboss.jandex.Indexer;
import org.jboss.jandex.MethodInfo;
import org.jboss.jandex.Type;

/**
 * @author lgao
 *
 */
public class Demos {
	
	private Demos(){}

	public class Demo {
		private String name;
		private String desp;
		
		private Method method;
		
		public String getName() {
			return name;
		}
		
		public String getDescription () {
			return desp;
		}
		
		public Method getMethod() {
			return method;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((method == null) ? 0 : method.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Demo other = (Demo) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (method == null) {
				if (other.method != null)
					return false;
			} else if (!method.equals(other.method))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		private Demos getOuterType() {
			return Demos.this;
		}
		
	}
	
	private final List<Demo> demos = new ArrayList<Demo>();
	
	public synchronized static Demos scanCurrentJar() {
		Demos demos = new Demos();
		try
		{
			demos.scanCurrentJarInner();
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
		return demos;
	}
	
	/**
	 * only check current jar file.
	 * Can't scan more than one jar file yet.
	 */
	private void scanCurrentJarInner() {
		URL baseURL = this.getClass().getClassLoader().getResource("org/jboss/demos/Demo.class");
		if (baseURL == null) {
			throw new IllegalStateException("Can't locate Demos, please check your class path");
		}
		String baseURLStr = baseURL.toExternalForm();
		final Indexer indexer = new Indexer();
		try {
			
			URL top = new URL(baseURLStr.substring(0, baseURLStr.indexOf("org/jboss/demos/Demo.class")));
			String topStr = top.toExternalForm();
			if (topStr.endsWith(".jar!/")) {
				// it is a jar
				scanJarURL(top, indexer);
			} else if (topStr.endsWith("/") && "file".equals(top.getProtocol())) {
				// it is a directory
				scanDir(new File(top.getFile()), indexer);
			} else {
				throw new IllegalStateException("Wrong state! " + topStr);
			}
		} catch (IOException e) {
			throw new RuntimeException("Wrong when scan demo annotations.", e);
		} catch (URISyntaxException e) {
			throw new RuntimeException("Wrong URI syntax.", e);
		}
		Index index = indexer.complete();
		try {
			composeDemos(index);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("No Class Found!", e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("No method found!", e);
		} catch (SecurityException e) {
			throw new RuntimeException("Security Exception!", e);
		}
	}

	private void composeDemos(Index index) throws ClassNotFoundException, NoSuchMethodException, SecurityException {
		List<AnnotationInstance> demoAnnoInstances = index.getAnnotations(DotName.createSimple(org.jboss.demos.Demo.class.getName()));
		for (AnnotationInstance ai: demoAnnoInstances) {
			AnnotationTarget target = ai.target();
			if (target instanceof MethodInfo) {
				MethodInfo m = (MethodInfo) target;
				String methodName = m.name();
				String declaringClassName = m.declaringClass().name().toString();
				List<String> parameterTypes = new ArrayList<String>(m.args().length);
				for (Type type : m.args()) {
					parameterTypes.add(type.toString());
				}
				ClassLoader cl = getCurrentClassLoder();
				Class<?> clazz = cl.loadClass(declaringClassName);
				Class<?>[] params = new Class<?>[parameterTypes.size()];
				int i = 0;
				for (String paramClazz : parameterTypes) {
					params[i] = cl.loadClass(paramClazz);
					i ++;
				}
				Method method = clazz.getDeclaredMethod(methodName, params);
				org.jboss.demos.Demo demoAnnotation = method.getAnnotation(org.jboss.demos.Demo.class);
				Demo demo = new Demo();
				demo.name = demoAnnotation.name();
				demo.desp = demoAnnotation.description();
				demo.method = method;
				if (!this.demos.contains(demo)) {
					demos.add(demo);
				}
			}
		}
	}

	private ClassLoader getCurrentClassLoder() {
		return getClass().getClassLoader();
	}

	private void scanJarURL(URL top, final Indexer indexer) throws IOException, URISyntaxException {
		String urlPath = top.getFile();
		urlPath = urlPath.substring("file:".length(), urlPath.indexOf("!/"));
		System.out.println(urlPath);
		JarFile jarFile = new JarFile(urlPath);
		try {
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry jarEntry = entries.nextElement();
				if (jarEntry.getName().endsWith(".class")) {
					InputStream is = null;
					try {
						is = jarFile.getInputStream(jarEntry);
						indexer.index(is);
					} finally {
						if (is != null) {
							is.close();
						}
					}
				}
			}
		} finally {
			jarFile.close();
		}
	}

	private void scanDir(File dir, final Indexer indexer) throws IOException {
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return (pathname.isFile() && pathname.getAbsolutePath().endsWith(".class"))
						|| pathname.isDirectory();
			}
		};
		for (File f: dir.listFiles(filter)) {
			if (f.isDirectory()) {
				scanDir(f, indexer);
			}
			else{
				FileInputStream input = null;
				try {
					input = new FileInputStream(f);
					indexer.index(input);
				} finally {
					if (input != null) {
						input.close();
					}
				}
			}
		}
	}

	public List<Demo> getDemos() {
		return Collections.unmodifiableList(demos);
	}
	
	public Demo getDemo(String demoName) {
		if (this.demos == null) {
			return null;
		}
		for (Demo demo: this.demos) {
			if (demo.getName().equals(demoName)) {
				return demo;
			}
		}
		return null;
	}
	
}
