// Generated code from Butter Knife. Do not modify!
package gtg.virus.gtpr;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class GTGEpubViewer$$ViewInjector {
  public static void inject(Finder finder, final gtg.virus.gtpr.GTGEpubViewer target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230887, "field 'mPager'");
    target.mPager = (android.support.v4.view.ViewPager) view;
  }

  public static void reset(gtg.virus.gtpr.GTGEpubViewer target) {
    target.mPager = null;
  }
}
