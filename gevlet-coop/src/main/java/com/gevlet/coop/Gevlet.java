package com.gevlet.coop;

import com.gevlet.coop.Utils.Strings;
import com.gevlet.coop.core.ServerContainer;
import com.gevlet.coop.exceptions.ServerInitException;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ClassName:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-08 21:28
 */
public class Gevlet implements Lifecycle {

    private ClassLoader commonClassloader;


    public void start(ClassLoader parent, String base) {
        if (Strings.isEmpty(base)) {
            throw new NullPointerException("base location 不能为空");
        }
        if (null == parent) {
            parent = ClassLoader.getSystemClassLoader();
        }
        //启动服务
        ServerContainer.getServerContainer(parent,base).startServers();
    }




    public void stop() {

    }


    public  class Repository {

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
