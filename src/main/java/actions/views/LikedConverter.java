package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Liked;

/**
 * いいねデータのDTOモデル⇔Viewモデルの変換を行うクラス
 *
 */
public class LikedConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param rv LikedViewのインスタンス
     * @return Likedのインスタンス
     */
    public static Liked toModel(LikedView lv) {
        return new Liked(
                lv.getId(),
                EmployeeConverter.toModel(lv.getEmployee()),
                ReportConverter.toModel(lv.getReport()),
                lv.getCreatedAt());
    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param l Liked のインスタンス
     * @return LikedViewのインスタンス
     */
    public static LikedView toView(Liked l) {

        if (l == null) {
            return null;
        }

        return new LikedView(
                l.getId(),
                EmployeeConverter.toView(l.getEmployee()),
                ReportConverter.toView(l.getReport()),
                l.getCreatedAt());
    }

    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    public static List<LikedView> toViewList(List<Liked> list) {
        List<LikedView> evs = new ArrayList<>();

        for (Liked l : list) {
            evs.add(toView(l));
        }

        return evs;
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param l DTOモデル(コピー先)
     * @param lv Viewモデル(コピー元)
     */
    public static void copyViewToModel(Liked l, LikedView lv) {
        l.setId(lv.getId());
        l.setEmployee(EmployeeConverter.toModel(lv.getEmployee()));
        l.setReport(ReportConverter.toModel(lv.getReport()));
        l.setCreatedAt(lv.getCreatedAt());

    }

}