// Generated code from Butter Knife. Do not modify!
package gtg.virus.gtpr;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ReadingPlanList$$ViewInjector {
  public static void inject(Finder finder, final gtg.virus.gtpr.ReadingPlanList target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230934, "field 'mListView'");
    target.mListView = (android.widget.ListView) view;
  }

  public static void reset(gtg.virus.gtpr.ReadingPlanList target) {
    target.mListView = null;
  }
}
