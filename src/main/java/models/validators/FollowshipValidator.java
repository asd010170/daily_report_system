package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.FollowshipView;

/**
 * いいねインスタンスに設定されている値のバリデーションを行うクラス
 */
public class FollowshipValidator {

    /**
     * 日報インスタンスの各項目についてバリデーションを行う
     * @param lv いいねインスタンス
     * @return エラーのリスト
     */
    public static List<String> validate(FollowshipView lv) {
        List<String> errors = new ArrayList<String>();

        return errors;
    }
}