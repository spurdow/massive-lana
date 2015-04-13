// Generated code from Butter Knife. Do not modify!
package gtg.virus.gtpr.adapters;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class AudioListAdapter$ViewHolder$$ViewInjector {
  public static void inject(Finder finder, final gtg.virus.gtpr.adapters.AudioListAdapter.ViewHolder target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230826, "field 'title'");
    target.title = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131230827, "field 'details'");
    target.details = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131230828, "field 'button'");
    target.button = (android.widget.Switch) view;
  }

  public static void reset(gtg.virus.gtpr.adapters.AudioListAdapter.ViewHolder target) {
    target.title = null;
    target.details = null;
    target.button = null;
  }
}
