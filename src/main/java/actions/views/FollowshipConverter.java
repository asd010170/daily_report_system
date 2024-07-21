package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Followship;

/**
 * 日報データのDTOモデル⇔Viewモデルの変換を行うクラス
 *
 */
public class FollowshipConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param rv ReportViewのインスタンス
     * @return Reportのインスタンス
     */
    public static Followship toModel(FollowshipView fv) {
        return new Followship(
                fv.getId(),
                EmployeeConverter.toModel(fv.getFollowee()),
                EmployeeConverter.toModel(fv.getFollower()),
                fv.getCreatedAt(),
                fv.getUpdatedAt());
    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param r Reportのインスタンス
     * @return ReportViewのインスタンス
     */
    public static FollowshipView toView(Followship f) {

        if (f == null) {
            return null;
        }

        return new FollowshipView(
                f.getId(),
                EmployeeConverter.toView(f.getFollowee()),
                EmployeeConverter.toView(f.getFollower()),
                f.getCreatedAt(),
                f.getUpdatedAt());
    }

    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    public static List<FollowshipView> toViewList(List<Followship> list) {
        List<FollowshipView> fvs = new ArrayList<>();

        for (Followship f : list) {
            fvs.add(toView(f));
        }

        return fvs;
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param r DTOモデル(コピー先)
     * @param rv Viewモデル(コピー元)
     */
    public static void copyViewToModel(Followship f, FollowshipView fv) {
        f.setId(fv.getId());
        f.setFollowee(EmployeeConverter.toModel(fv.getFollowee()));
        f.setFollower(EmployeeConverter.toModel(fv.getFollower()));
        f.setCreatedAt(fv.getCreatedAt());
        f.setUpdatedAt(fv.getUpdatedAt());

    }

}