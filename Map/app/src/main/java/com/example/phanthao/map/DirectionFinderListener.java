package com.example.phanthao.map;

import java.util.List;

/**
 * Created by Phan Thao on 4/24/2018.
 */

public interface DirectionFinderListener {
    public abstract void onDirectionFinderStart();
    public abstract void onDirectionFinderSuccess(List<Route> route);
}
