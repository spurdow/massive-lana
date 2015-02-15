// Generated code from Butter Knife. Do not modify!
package gtg.virus.gtpr;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class LoginFragment$$ViewInjector {
  public static void inject(Finder finder, final gtg.virus.gtpr.LoginFragment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230876, "field 'authButton' and method 'onAuthButton'");
    target.authButton = (android.widget.Button) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onAuthButton();
        }
      });
    view = finder.findRequiredView(source, 2131230741, "field 'loginButton' and method 'onLogin'");
    target.loginButton = (info.hoang8f.widget.FButton) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onLogin();
        }
      });
    view = finder.findRequiredView(source, 2131230873, "field 'mUsername'");
    target.mUsername = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131230874, "field 'mPassword'");
    target.mPassword = (android.widget.TextView) view;
  }

  public static void reset(gtg.virus.gtpr.LoginFragment target) {
    target.authButton = null;
    target.loginButton = null;
    target.mUsername = null;
    target.mPassword = null;
  }
}
