// Generated code from Butter Knife. Do not modify!
package gtg.virus.gtpr;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ScheduledBooksList$$ViewInjector {
  public static void inject(Finder finder, final gtg.virus.gtpr.ScheduledBooksList target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230938, "field 'mListView'");
    target.mListView = (android.widget.ListView) view;
  }

  public static void reset(gtg.virus.gtpr.ScheduledBooksList target) {
    target.mListView = null;
  }
}
