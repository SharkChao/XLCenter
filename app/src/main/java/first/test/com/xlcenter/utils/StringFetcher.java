package first.test.com.xlcenter.utils;


import first.test.com.xlcenter.base.MyLeanCloudApp;

/**
 */
public class StringFetcher {
    public static String getString(int id) {
        return MyLeanCloudApp.getInstance().getString(id);
    }
}
