package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.LikedView;

/**
 * いいねインスタンスに設定されている値のバリデーションを行うクラス
 */
public class LikedValidator {

    /**
     * 日報インスタンスの各項目についてバリデーションを行う
     * @param lv いいねインスタンス
     * @return エラーのリスト
     */
    public static List<String> validate(LikedView lv) {
        List<String> errors = new ArrayList<String>();

        return errors;
    }
}