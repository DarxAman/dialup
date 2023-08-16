package com.dialupdelta.ui.sleep_enhancer;

import androidx.fragment.app.FragmentManager;

public class CommonObject {

    public FragmentManager fragmentManager;
    public static FragmentManager fragmentManagerContext ;

    public CommonObject() {

    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }
}
