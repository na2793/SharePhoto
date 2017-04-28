package com.study.hancom.sharephoto.util.shareUtil;

import java.io.File;
import java.util.List;

public interface IShareStrategy {
    void perform(File file, List params);
}
