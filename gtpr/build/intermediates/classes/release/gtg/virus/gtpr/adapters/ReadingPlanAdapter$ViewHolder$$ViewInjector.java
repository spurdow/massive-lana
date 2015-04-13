// Generated code from Butter Knife. Do not modify!
package gtg.virus.gtpr.adapters;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ReadingPlanAdapter$ViewHolder$$ViewInjector {
  public static void inject(Finder finder, final gtg.virus.gtpr.adapters.ReadingPlanAdapter.ViewHolder target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230832, "field 'bookImage'");
    target.bookImage = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131230833, "field 'bookSample'");
    target.bookSample = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131230935, "field 'bookStatus'");
    target.bookStatus = (android.widget.TextView) view;
  }

  public static void reset(gtg.virus.gtpr.adapters.ReadingPlanAdapter.ViewHolder target) {
    target.bookImage = null;
    target.bookSample = null;
    target.bookStatus = null;
  }
}
