package in.app.currentaffairsquiz;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SliderViewPager extends PagerAdapter {
    Context context;
    ArrayList<SliderDataModel> imagesUrl;
    LayoutInflater layoutInflater;


    public SliderViewPager(final ViewPager pager, Context context, final ArrayList<SliderDataModel> imageUrl) {
        this.context = context;
        this.imagesUrl = imageUrl;
        layoutInflater = LayoutInflater.from(context);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
//                int pageCount = imageUrl.size();
//                if (position == 0){
//                    pager.setCurrentItem(pageCount,false);
//                } else if (position == pageCount-1){
//                    pager.setCurrentItem(1,false);
//                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO Auto-generated method stub
            }
        });
    }


    @Override
    public int getCount() {
        return imagesUrl.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.item_slider_viewpager, container, false);

        final ImageView imageView = (ImageView) itemView
                .findViewById(R.id.slider_imageView);

        Glide.with(context)
                .load(imagesUrl.get(position).getImage())
                .into(imageView);

        container.addView(itemView,0);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}