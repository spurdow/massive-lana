// Generated code from Butter Knife. Do not modify!
package gtg.virus.gtpr.adapters;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class BookmarkAdapter$ViewHolder$$ViewInjector {
  public static void inject(Finder finder, final gtg.virus.gtpr.adapters.BookmarkAdapter.ViewHolder target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230832, "field 'bookImage'");
    target.bookImage = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131230833, "field 'bookName'");
    target.bookName = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131230834, "field 'bookSample'");
    target.bookSample = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131230835, "field 'bookPage'");
    target.bookPage = (android.widget.TextView) view;
  }

  public static void reset(gtg.virus.gtpr.adapters.BookmarkAdapter.ViewHolder target) {
    target.bookImage = null;
    target.bookName = null;
    target.bookSample = null;
    target.bookPage = null;
  }
}
