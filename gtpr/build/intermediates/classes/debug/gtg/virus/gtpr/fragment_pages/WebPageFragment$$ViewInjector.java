// Generated code from Butter Knife. Do not modify!
package gtg.virus.gtpr.fragment_pages;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class WebPageFragment$$ViewInjector {
  public static void inject(Finder finder, final gtg.virus.gtpr.fragment_pages.WebPageFragment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230869, "field 'mWebView'");
    target.mWebView = (android.webkit.WebView) view;
    view = finder.findRequiredView(source, 2131230870, "field 'mToggle' and method 'onBookMarked'");
    target.mToggle = (android.widget.ToggleButton) view;
    ((android.widget.CompoundButton) view).setOnCheckedChangeListener(
      new android.widget.CompoundButton.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(
          android.widget.CompoundButton p0,
          boolean p1
        ) {
          target.onBookMarked(p1);
        }
      });
  }

  public static void reset(gtg.virus.gtpr.fragment_pages.WebPageFragment target) {
    target.mWebView = null;
    target.mToggle = null;
  }
}