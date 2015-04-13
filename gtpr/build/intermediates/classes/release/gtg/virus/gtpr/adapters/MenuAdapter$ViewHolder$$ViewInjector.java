// Generated code from Butter Knife. Do not modify!
package gtg.virus.gtpr.adapters;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MenuAdapter$ViewHolder$$ViewInjector {
  public static void inject(Finder finder, final gtg.virus.gtpr.adapters.MenuAdapter.ViewHolder target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230910, "field 'mImgView'");
    target.mImgView = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131230911, "field 'mTitle'");
    target.mTitle = (android.widget.TextView) view;
  }

  public static void reset(gtg.virus.gtpr.adapters.MenuAdapter.ViewHolder target) {
    target.mImgView = null;
    target.mTitle = null;
  }
}
