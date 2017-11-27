package top.zeroyiq.master_help_me.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;
import java.util.Map;

import top.zeroyiq.master_help_me.fragments.QuestionFragment;

/**
 * Created by ZeroyiQ on 2017/9/8.
 */

public class TabAdapter extends FragmentPagerAdapter{
    private List<String> list;
    private Map<String, String> questionsPathMap;

    public TabAdapter(FragmentManager fm, List<String> list, Map<String, String> questionsPathMap) {
        super(fm);
        this.list = list;
        this.questionsPathMap = questionsPathMap;
    }

    @Override
    public Fragment getItem(int position) {
        return QuestionFragment.newsInstance(list.get(position),questionsPathMap.get(list.get(position)));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }
    //    private LayoutInflater inflater;

//
//
//    public TabAdapter(FragmentManager fragmentManager, Context context, Map<String, String> questionsPathMap) {
//        super(fragmentManager);
//        inflater = LayoutInflater.from(context);
//        this.questionsPathMap = questionsPathMap;
//    }
//
//    @Override
//    public int getCount() {
//        return questionsPathMap.size();
//    }
//
//    /**
//     * 填充tab
//     */
//    @Override
//    public View getViewForTab(int position, View convertView, ViewGroup container) {
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.tab_category, container, false);
//        }
//        TextView textView = (TextView) convertView;
//        textView.setText(questionsCategory[position % questionsCategory.length]);
//        return convertView;
//    }
//
//    /**
//     * 填充viewpager中的fragment
//     */
//    @Override
//    public android.support.v4.app.Fragment getFragmentForPage(int position) {
//        String fragmentName = questionsCategory[position % questionsCategory.length];
//        QuestionFragment questionsListFragment = new QuestionFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("fragmentName", fragmentName);
//        bundle.putString("categoryPath", questionsPathMap.get(fragmentName));
//        questionsListFragment.setArguments(bundle);
//        return questionsListFragment;
//    }

}
