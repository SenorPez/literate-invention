package com.senorpez.projectcars.packetcapture;

class ApplicationInfo {
    static String getImplementationTitle() {
        return ApplicationInfo.class.getPackage().getImplementationTitle();
    }
    static String getImplementationVersion() {
        return ApplicationInfo.class.getPackage().getImplementationVersion();
    }
}
