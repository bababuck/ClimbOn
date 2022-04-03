package com.example.climbon;

import android.util.Log;

import java.util.ArrayList;

public class HoldHash {

    private ArrayList<ArrayList<ArrayList<ThreeDeeHold>>> holds;

    float width, height, left_edge, top_edge;

    private int num_horizontal_splits = 0, num_vertical_splits = 0;

    public HoldHash(ArrayList<ThreeDeeHold> holds, float left, float top, float right, float bottom){
        this.holds = new ArrayList<>();
        this.holds.add(new ArrayList<>());
        this.holds.get(0).add(holds);
        width = right - left;
        height = top - bottom;
        left_edge = left;
        top_edge = top;
        dynamicSplit();
    }

    private void dynamicSplit() {
        boolean horizontal_split = true;
        boolean vertical_split = true;
        int new_total_holds, total_holds = holds.get(0).get(0).size();
//        Log.e("HoldHash", "Start: " + total_holds);
        while (horizontal_split || vertical_split) {
            if (horizontal_split) {
                ++num_horizontal_splits;
                new_total_holds = total_holds + split_horizontal();
//                Log.e("HoldHash", "Hor: " + new_total_holds);
                if ((2 * total_holds) == new_total_holds)
                    horizontal_split  = false;
                else
                    total_holds = new_total_holds;
            }
            if (vertical_split) {
                ++num_vertical_splits;
                new_total_holds = total_holds + split_vertical();
//                Log.e("HoldHash", "Vert: " + new_total_holds);
                if ((2 * total_holds) == new_total_holds)
                    vertical_split  = false;
                else
                    total_holds = new_total_holds;
            }
        }
    }

    private int split_vertical() {
        int size_change = 0;
        int loops = holds.size();
        for (int i=0; i<loops; ++i){
            holds.add(2*i+1, new ArrayList<ArrayList<ThreeDeeHold>>());
            for (int j = 0; j< holds.get(0).size(); ++j){
                holds.get(2*i+1).add(new ArrayList<>());
                float bin_size = height / (float) Math.pow(2, num_vertical_splits);
                float bin_top = top_edge - j * bin_size;
                float bin_mid = bin_top - bin_size;
                float bin_bottom = bin_mid - bin_size;
                for (int k = 0; k< holds.get(2 * i).get(j).size(); ++k) {
                    ThreeDeeHold hold = holds.get(2*i).get(j).get(k);
                    if (!(hold.top_bound >= bin_mid && hold.bottom_bound <= bin_top)) {
                        holds.get(2*i).get(j).remove(k);
//                        Log.e("HoldHash", k + ": dropped");
                        --k;
                        --size_change;
                    }
                    if (hold.top_bound >= bin_bottom && hold.bottom_bound <= bin_mid) {
                        holds.get(2*i+1).get(j).add(hold);
                        ++size_change;
//                        Log.e("HoldHash", k + ": added");
                    }
                }
            }
        }
        Log.e("HoldHash", holds.size() + ": vert len");
        return size_change;
    }

    private int split_horizontal() {
        int size_change = 0;
        int loops = holds.get(0).size();
        for (int i=0; i<holds.size(); ++i){
            for (int j=0; j<loops; ++j){
//                Log.e("HoldHash", i + "  " + j);
                holds.get(i).add(2*j+1, new ArrayList<ThreeDeeHold>());
                float bin_size = width / (float) Math.pow(2, num_horizontal_splits);
                float bin_left = left_edge + j * bin_size;
                float bin_mid = bin_left + bin_size;
                float bin_right = bin_mid + bin_size;
                for (int k = 0; k< holds.get(i).get(2 * j).size(); ++k) { // will this change
                    ThreeDeeHold hold = holds.get(i).get(2 * j).get(k);
//                    Log.e("HoldHash", "left: "+hold.left_bound + "right: "+hold.right_bound);
//                    Log.e("HoldHash", "bin_right: "+bin_right+"bin_mid: "+bin_mid + "bin_left: "+bin_left);
                    if (!(hold.left_bound <= bin_mid && hold.right_bound >= bin_left)) {
                        holds.get(i).get(2 * j).remove(k);
//                        Log.e("HoldHash", k + ": dropped");
                        --k;
                        --size_change;
                    }
                    if (hold.left_bound <= bin_right && hold.right_bound >= bin_mid) {
                        holds.get(i).get(2 * j + 1).add(hold);
                        ++size_change;
//                        Log.e("HoldHash", k + ": added");
                    }
                }
            }
        }
        return size_change;
    }

    public boolean findClickedHold(float click_x, float click_y) {
        Log.e("HoldHash", "w: "+width+" h: "+height);
        Log.e("HoldHash", "left: "+left_edge+" top: "+top_edge);
        Log.e("HoldHash", "click_x: "+click_x+" click_y: "+click_y);
        Log.e("HoldHash", "hori: "+num_horizontal_splits+" vert: "+num_vertical_splits);
        int click_region_x = (int) ((click_x - left_edge) / width * Math.pow(2, num_horizontal_splits));
        int click_region_y = (int) ((top_edge - click_y) / height * Math.pow(2, num_vertical_splits));
        Log.e("HoldHash", "x: "+click_region_x+" y: "+click_region_y);
        for (ThreeDeeHold hold : holds.get(click_region_y).get(click_region_x)) {
            if (hold.contains2D(click_x, click_y)) {
                hold.setColor("Blue");
                return true;
            }
        }
        return false;
    }
}
