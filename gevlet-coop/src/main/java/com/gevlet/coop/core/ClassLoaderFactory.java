package com.gevlet.coop.core;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

/**
 * ClassName:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-09 12:35
 */
public final class ClassLoaderFactory {


    /**
     * 创建类加载器
     *
     * @param packages
     * @param parent
     * @return
     */
    public static ClassLoader createClassLoader(File packages[], final ClassLoader parent) {


        return null;
    }

    /**
     * 创建类加载器
     *
     * @param fPackage
     * @param parent
     * @return
     */
    public static ClassLoader createClassLoader(File fPackage, final ClassLoader parent) {


        return null;
    }


    public static ClassLoader createClassLoader(List<ServerContainer.Repository> repositorys, final ClassLoader parent) {
        try {
            final URL[] array = buldPackageURL(repositorys);
            return AccessController.doPrivileged(
                    new PrivilegedAction<URLClassLoader>() {
                        @Override
                        public URLClassLoader run() {
                            URLClassLoader classLoader = null;
                            if (parent == null) {
                                classLoader = new URLClassLoader(array);
                            } else {
                                classLoader = new URLClassLoader(array, parent);
                            }
                            return classLoader;
                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return parent;
    }



    public static ClassLoader createClassLoader(ServerContainer.Repository repository, final ClassLoader parent) {

        try {
            URL url = repository.getPackageFile().toURI().toURL();
            final URL[] array = new URL[]{url};
            return AccessController.doPrivileged(
                    new PrivilegedAction<URLClassLoader>() {
                        @Override
                        public URLClassLoader run() {
                            URLClassLoader classLoader = null;
                            if (parent == null) {
                                classLoader = new URLClassLoader(array);
                            } else {
                                classLoader = new URLClassLoader(array, parent);
                            }
                            return classLoader;
                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return parent;
    }


    private static URL[] buldPackageURL(List<ServerContainer.Repository> repositorys) {
        URL[] array = new URL[repositorys.size()];
        for (int i = 0; i < repositorys.size(); i++) {
            try {
                URL url = repositorys.get(i).getPackageFile().toURI().toURL();
                array[i] = url;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return array;
    }

}
