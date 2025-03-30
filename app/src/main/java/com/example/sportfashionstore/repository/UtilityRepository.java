package com.example.sportfashionstore.repository;

import com.example.sportfashionstore.R;
import com.example.sportfashionstore.model.FeatureModel;

import java.util.ArrayList;
import java.util.List;

public class UtilityRepository {
    public List<FeatureModel> getAccountSettingList() {
        List<FeatureModel> accSettingList = new ArrayList<>();
        accSettingList.add(new FeatureModel("Đổi vai trò", R.drawable.change_role_icon));
        accSettingList.add(new FeatureModel("Chỉnh sửa thông tin", R.drawable.edit_profile_icon));
        accSettingList.add(new FeatureModel("Đổi mật khẩu", R.drawable.change_password_icon));
        accSettingList.add(new FeatureModel("Đăng xuất", R.drawable.log_out_icon));
        accSettingList.add(new FeatureModel("Xóa tài khoản", R.drawable.delete_account_icon));

        return accSettingList;
    }

    public List<FeatureModel> getCommonSettingList() {
        List<FeatureModel> commonSettingList = new ArrayList<>();
        commonSettingList.add(new FeatureModel("Ngôn ngữ", R.drawable.language_icon));
        commonSettingList.add(new FeatureModel("Chủ đề", R.drawable.theme_icon));

        return commonSettingList;
    }
}
