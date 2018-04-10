package first.test.com.xlcenter.inject.component;


import javax.inject.Singleton;

import dagger.Component;
import first.test.com.xlcenter.base.MyLeanCloudApp;
import first.test.com.xlcenter.inject.module.ApiServiceModule;
import first.test.com.xlcenter.presenter.MainPresenter;

/**
 */
@Singleton
@Component(modules = {
        ApiServiceModule.class
})
public interface AppComponent {
    MainPresenter getMainPresenter();
    void inject(MyLeanCloudApp myLeanCloudApp);
}
