package com.example.phanthao.map;

import java.util.List;

/**
 * Created by Phan Thao on 4/29/2018.
 */

interface DirectionFinderListener {
    void onDirectionFinderStart();

    void onDirectionFinderSuccess(List<Route> routes);
}
