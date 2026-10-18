package made.by.human.tiktokantiburn.settings;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import made.by.human.tiktokantiburn.RootCheck;

public class PageAdapter extends FragmentStateAdapter {
    boolean haveRoot = false;
    public int tabsCount = 3; // with root



    AdditionalSettingsFragment AdditionalSettings;
    LSPosedSettingsFragment LSPosedSettings;
    MainSettingsFragment MainSettings;

    public PageAdapter(@NonNull FragmentActivity activity) {
        super(activity);
    }

    public void init() {
        Boolean cached = RootCheck.getCached();
        haveRoot = cached != null && cached;
        AdditionalSettings = new AdditionalSettingsFragment();
        LSPosedSettings = new LSPosedSettingsFragment();
        MainSettings = new MainSettingsFragment();
    }
    public void setHaveRoot(boolean root) {
        if (root == haveRoot) return;
        Log.d("RootChecker", "Have Root: "+root);
        haveRoot = root;
        if (root) {
            notifyItemInserted(tabsCount - 1);
        } else {
            notifyItemRemoved(tabsCount - 1);
        }
    }


    public void onResumeNotify() {
        LSPosedSettings.onResumeNotify();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1: return AdditionalSettings;
            case 2: return LSPosedSettings;
            default: return MainSettings; // 0
        }
    }

    @Override
    public int getItemCount() {
        return haveRoot ?
                tabsCount :
                tabsCount-1;
    }
}
