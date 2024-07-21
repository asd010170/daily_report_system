package services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import actions.views.LikedConverter;
import actions.views.LikedView;
import constants.JpaConst;
import models.Employee;
import models.validators.LikedValidator;

/**
 * 日報テーブルの操作に関わる処理を行うクラス
 */
public class LikedService extends ServiceBase {

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
     * 指定した日報がいいねされた従業員を取得し、返却する
     * @param reportId
     * @return いいねを押した従業員
     */
    public String showAllRep(int reportId) {
     // 名前付きクエリの返り値の型を Employee に変更
        List<Integer> employeeIds = em.createNamedQuery(JpaConst.Q_LIK_GET_USERS_BY_REPORT, Integer.class)
                .setParameter(JpaConst.JPQL_PARM_REPORT_ID, reportId)
                .getResultList();

     // 従業員IDを基に従業員エンティティのリストを取得
        List<Employee> employees = employeeIds.stream()
                .map(id -> em.find(Employee.class, id))
                .collect(Collectors.toList());

     // 結果を String に変換
        String showEmployee = employees.stream()
                .map(Employee::getName) // Employee の名前を取得
                .collect(Collectors.joining(", ")); // カンマ区切りで連結

        return showEmployee;
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
     * いいねデータを1件登録する
     * @param lv いいねデータ
     */
    private void createInternal(LikedView lv) {

        em.getTransaction().begin();
        em.persist(LikedConverter.toModel(lv));
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