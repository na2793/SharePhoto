package com.study.hancom.sharephoto.util.shareUtil;

import java.io.File;
import java.util.List;

public class ShareManager {
    public static void share(File file, List params, IShareStrategy strategy) {
        strategy.perform(file, params);
    }
}
