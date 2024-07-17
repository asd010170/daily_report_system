package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.LikedConverter;
import actions.views.LikedView;
import constants.JpaConst;
import models.Liked;
import models.validators.LikedValidator;

/**
 * 日報テーブルの操作に関わる処理を行うクラス
 */
public class LikedService extends ServiceBase {

    /**
     * 指定した従業員が作成した日報データの件数を取得し、返却する
     * @param employee
     * @return 日報データの件数
     */
    public long countAllMine(EmployeeView employee) {

        long count = (long) em.createNamedQuery(JpaConst.Q_LIK_GET_USERS_BY_REPORT, Long.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .getSingleResult();

        return count;
    }

    /**
     * 指定した日報がいいねされたデータの件数を取得し、返却する
     * @param employee
     * @return 日報データの件数
     */
    public long countAllRep(int reportId) {

        long count = (long) em.createNamedQuery(JpaConst.Q_LIK_COUNT_BY_REPORT, Long.class)
                .setParameter(JpaConst.JPQL_PARM_REPORT_ID, reportId)
                .getSingleResult();

        return count;
    }

    /**
     * いいねテーブルのデータの件数を取得し、返却する
     * @return データの件数
     */
    public long countAll() {
        long likes_count = (long) em.createNamedQuery(JpaConst.Q_LIK_COUNT_BY_REPORT, Long.class)
                .getSingleResult();
        return likes_count;
    }

    /**
     * idを条件に取得したデータをLikedViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public LikedView findOne(int id) {
        return LikedConverter.toView(findOneInternal(id));
    }

    /**
     * 画面から入力された登録内容を元にデータを1件作成し、いいねテーブルに登録する
     * @param lv 日報の登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> create(LikedView lv) {
        List<String> errors = LikedValidator.validate(lv);
        if (errors.size() == 0) {
            LocalDateTime ldt = LocalDateTime.now();
            lv.setCreatedAt(ldt);
            createInternal(lv);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * 画面から入力された登録内容を元に、いいねデータを更新する
     * @param lv 日報の更新内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> update(LikedView lv) {

        //バリデーションを行う
        List<String> errors = LikedValidator.validate(lv);

        if (errors.size() == 0) {

            //更新日時を現在時刻に設定
            LocalDateTime ldt = LocalDateTime.now();

            updateInternal(lv);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * idを条件にデータを1件取得する
     * @param id
     * @return 取得データのインスタンス
     */
    private Liked findOneInternal(int id) {
        return em.find(Liked.class, id);
    }

    /**
     * いいねデータを1件登録する
     * @param lv いいねデータ
     */
    private void createInternal(LikedView lv) {

        em.getTransaction().begin();
        em.persist(LikedConverter.toModel(lv));
        em.getTransaction().commit();

    }

    /**
     * いいねデータを更新する
     * @param lv いいねデータ
     */
    private void updateInternal(LikedView lv) {

        em.getTransaction().begin();
        Liked l = findOneInternal(lv.getId());
        LikedConverter.copyViewToModel(l, lv);
        em.getTransaction().commit();

    }

    /**
     * 特定の従業員が特定の日報に「いいね」しているかどうかを確認する
     * @param employeeId 従業員ID
     * @param reportId 日報ID
     * @return 既に「いいね」している場合はtrue、そうでない場合はfalse
     */
    public boolean hasLiked(int employeeId, int reportId) {
        long count = em.createNamedQuery(JpaConst.Q_LIK_COUNT_BY_EMP_AND_REP, Long.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, employeeId)
                .setParameter(JpaConst.JPQL_PARM_REPORT_ID, reportId)
                .getSingleResult();

        return count > 0;
    }

}