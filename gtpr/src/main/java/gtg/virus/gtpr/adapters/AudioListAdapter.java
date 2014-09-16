package gtg.virus.gtpr.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import gtg.virus.gtpr.R;
import gtg.virus.gtpr.entities.Audio;


public class AudioListAdapter extends AbstractListAdapter<Audio>{

    private MediaPlayer mPlayer = null;

    public AudioListAdapter(Context context, List<Audio> lists) {
        super(context, lists);
    }

    /**
     * returns the Override view provided by its subclass
     *
     * @param position
     * @param child
     * @param root
     */
    @Override
    public View getOverridedView(int position, View child, ViewGroup root) {
        ViewHolder vH = null;
        if(child == null){
            child = mInflater.inflate(R.layout.audio_list_row , null);
            vH = new ViewHolder();
            vH.title = (TextView) child.findViewById(R.id.txt_audio_list_title);
            vH.details = (TextView) child.findViewById(R.id.txt_audio_list_details);
            vH.button = (Switch) child.findViewById(R.id.play_stop_switch);

            child.setTag(vH);
        }else{
            vH = (ViewHolder) child.getTag();
        }

        final Audio audio = getObject(position);

        vH.title.setText(audio.getTitle());
        vH.details.setText(audio.getDetails());
        vH.button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        return child;
    }

    /**
     * <p>Returns a filter that can be used to constrain data with a filtering
     * pattern.</p>
     * <p/>
     * <p>This method is usually implemented by {@link android.widget.Adapter}
     * classes.</p>
     *
     * @return a filter used to constrain data
     */
    @Override
    public Filter getFilter() {
        return null;
    }

    private class ViewHolder {
        TextView title;
        TextView details;
        Switch button;
    }

}
