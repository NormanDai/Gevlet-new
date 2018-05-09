package com.gevlet.coop.core;

import com.gevlet.coop.connector.NetServerConnector;
import com.gevlet.coop.exceptions.ServerInitException;
import com.gevlet.coop.exceptions.ServerLoadeException;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-08 21:17
 */
public class ServerContainer {

    private static volatile ServerContainer serverContainer;

    private ConcurrentHashMap<String, ServerHandler> serverHandlers = new ConcurrentHashMap<>();

    private ServerContainer() {
    }

    public static synchronized ServerContainer getServerContainer() {
        if (null == serverContainer) {
            throw new ServerLoadeException("获取应用容器失败，容器为null");
        }
        return serverContainer;
    }

    public static synchronized ServerContainer getServerContainer(ClassLoader parentClassloader, String baseLocation) {
        if (null == serverContainer) {
            serverContainer = new ServerContainer();
            serverContainer.loadServers(parentClassloader, baseLocation);
        }
        return serverContainer;
    }

    public ServerHandler getServerHandler(String application) {
        return serverHandlers.get(application);
    }

    /**
     * 启动所有的服务
     */
    public void startServers() {

        NetServerConnector.main(new String[]{});

        for (;;){
            try {
                Thread.sleep(1);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    /**
     * 加载所有的应用服务
     *
     * @param parentClassloader
     * @param baseLocation
     */
    private void loadServers(ClassLoader parentClassloader, String baseLocation) {
        for (Repository repository : locadRepositorys(baseLocation)) {

            //处理每一个应用
            List<Repository> applicationRepositorys = getApplicationRepositorys(repository);
            ClassLoader serverClassLoader = ClassLoaderFactory.createClassLoader(applicationRepositorys, parentClassloader);
            ServerHandler serverHandler = HttpRestServerHandler.getServer(serverClassLoader);
            serverHandlers.put(repository.getApplication(), serverHandler);

//            ClassLoader serverClassLoader = ClassLoaderFactory.createClassLoader(repository, parentClassloader);
//            ServerHandler serverHandler = HttpRestServerHandler.getServer(serverClassLoader);
//            serverHandlers.put(repository.getApplication(), serverHandler);
        }
    }


    private List<Repository> getApplicationRepositorys(Repository repository) {
        List<Repository> repositories = new ArrayList<>();
        String location = repository.getLocation();
        File appLocation = new File(location);
        if (appLocation.exists() && appLocation.isDirectory()) {
            //处理 lib
            String libJarLocation = location + File.separator + "lib";
            File libJarsFile = new File(libJarLocation);
            if (libJarsFile.exists() && libJarsFile.isDirectory()) {

                File[] jarFiles = libJarsFile.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".jar");
                    }
                });

                for (int i = 0; i < jarFiles.length; i++) {
                    File jarFile = jarFiles[i];
                    Repository libJarRepository = new Repository(jarFile.getAbsolutePath(), jarFile, repository.application);
                    repositories.add(libJarRepository);
                }
            }

            //处理 app jar
            File packageFile = repository.getPackageFile();
            try {
                Repository appJar = new Repository(packageFile.getAbsolutePath(), packageFile, repository.application);
                repositories.add(appJar);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return repositories;
    }


    private List<Repository> locadRepositorys(String base) {

        File appHomeBaseFile = new File(base);
        List<Repository> packageFiles = new ArrayList<>();
        if (appHomeBaseFile.exists() && appHomeBaseFile.isDirectory()) {
            List<File> applicationsPaths = Arrays.asList(appHomeBaseFile.listFiles());
            for (File path : applicationsPaths) {
                String application = path.getName();
                String applicationPath =
                        appHomeBaseFile.getAbsolutePath()
                                + File.separator + application;
                String applicationPackagePath = applicationPath + File.separator + application + ".jar";
                File applicationPackageFile = new File(applicationPackagePath);
                if (!applicationPackageFile.exists()) {
                    throw new ServerInitException("找不到应用的jar包");
                }
                Repository repository = new Repository(applicationPath, applicationPackageFile, application);
                packageFiles.add(repository);
            }
        }
        return packageFiles;
    }

    public class Repository {

        private String location;

        private File packageFile;

        private String application;


        public Repository(String location, File packageFile, String application) {
            this.location = location;
            this.packageFile = packageFile;
            this.application = application;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public File getPackageFile() {
            return packageFile;
        }

        public void setPackageFile(File packageFile) {
            this.packageFile = packageFile;
        }

        public String getApplication() {
            return application;
        }

        public void setApplication(String application) {
            this.application = application;
        }

        @Override
        public String toString() {
            return "Repository{" +
                    "location='" + location + '\'' +
                    ", packageFile=" + packageFile +
                    ", application='" + application + '\'' +
                    '}';
        }
    }

}
