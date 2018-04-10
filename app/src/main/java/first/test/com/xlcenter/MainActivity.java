package first.test.com.xlcenter;

import android.databinding.ViewDataBinding;

import first.test.com.xlcenter.annotation.ContentView;
import first.test.com.xlcenter.base.BaseActivity;
import first.test.com.xlcenter.presenter.MainPresenter;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity<MainPresenter.MainUiCallback> implements MainPresenter.MainUi {


    @Override
    public void initTitle() {
        isShowToolBar(true);
        setCenterTitle("首页");
    }

    @Override
    public void initView(ViewDataBinding viewDataBinding) {

    }

    @Override
    public void initData() {

    }

    @Override
    protected void initEvent() {

    }
}
