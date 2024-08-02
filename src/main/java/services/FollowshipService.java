package services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import actions.views.EmployeeView;
import actions.views.FollowshipConverter;
import actions.views.FollowshipView;
import constants.JpaConst;
import models.Employee;
import models.Followship;
import models.validators.FollowshipValidator;

/**
 * フォローシップテーブルの操作に関わる処理を行うクラス
 */
public class FollowshipService extends ServiceBase {

    /**
     * フォロイーデータの件数を取得し、返却する
     * @param employee
     * @return 日報データの件数
     */
    public long countAllFee(int followeeId) {

        long count = (long) em.createNamedQuery(JpaConst.Q_FOL_COUNT_ALL_FEE, Long.class)
                .setParameter(JpaConst.JPQL_PARM_FOLLOWEE_ID, followeeId)
                .getSingleResult();

        return count;
    }

    /**
     * フォロイーデータの件数を取得し、返却する
     * @param employee
     * @return 日報データの件数
     */
    public long countAllFer(int followerId) {

        long count = (long) em.createNamedQuery(JpaConst.Q_FOL_COUNT_ALL_FER, Long.class)
                .setParameter(JpaConst.JPQL_PARM_FOLLOWER_ID, followerId)
                .getSingleResult();

        return count;
    }

    /**
     * フォロイーを取得し、返却する
     * @param followeeId
     * @return フォローを押した従業員
     */
    public String showAllFee(int followeeId) {
     // 名前付きクエリの返り値の型を Employee に変更
        List<Integer> employeeIds = em.createNamedQuery(JpaConst.Q_FOL_GET_USERS_BY_FEE, Integer.class)
                .setParameter(JpaConst.JPQL_PARM_FOLLOWEE_ID, followeeId)
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
     * フォロイーを取得し、返却する
     * @param followerId
     * @return フォローを押された従業員
     */
    public String showAllFer(int followerId) {
     // 名前付きクエリの返り値の型を Employee に変更
        List<Integer> employeeIds = em.createNamedQuery(JpaConst.Q_FOL_GET_USERS_BY_FER, Integer.class)
                .setParameter(JpaConst.JPQL_PARM_FOLLOWER_ID, followerId)
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
     * フォロイーを取得し、返却する
     * @param followeeId フォロワーID
     * @return フォローを押された従業員のリスト
     */
    public List<EmployeeView> showAllFeeE(int followeeId) {
        // 名前付きクエリの返り値の型を Integer に変更
        List<Integer> employeeIds = em.createNamedQuery(JpaConst.Q_FOL_GET_USERS_BY_FEE, Integer.class)
                .setParameter(JpaConst.JPQL_PARM_FOLLOWEE_ID, followeeId)
                .getResultList();

        // 従業員IDを基に従業員エンティティのリストを取得
        List<Employee> employees = employeeIds.stream()
                .map(id -> em.find(Employee.class, id))
                .collect(Collectors.toList());

        // Employee -> EmployeeView への変換メソッドを利用して変換
        List<EmployeeView> employeeViews = employees.stream()
                .map(this::toEmployeeView)
                .collect(Collectors.toList());

        return employeeViews;
    }

    /**
     * idを条件に取得したデータをEmployeeViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public FollowshipView findOne(int id) {
        Followship f = findOneInternal(id);
        return FollowshipConverter.toView(f);
    }

    /**
     * 画面から入力された登録内容を元にデータを1件作成し、いいねテーブルに登録する
     * @param lv 日報の登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> create(FollowshipView fv) {
        List<String> errors = FollowshipValidator.validate(fv);
        if (errors.size() == 0) {
            LocalDateTime ldt = LocalDateTime.now();
            fv.setCreatedAt(ldt);
            fv.setUpdatedAt(ldt);
            createInternal(fv);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * フォロー情報を削除する
     * @param followerId フォロワーのID
     * @param followeeId フォロイーのID
     */
    public void delete(int followeeId, int followerId) {
            // フォロー情報を検索
            Followship followship = em.createNamedQuery(JpaConst.Q_FOL_GET_BY_FEE_AND_FER, Followship.class)
                                      .setParameter(JpaConst.JPQL_PARM_FOLLOWEE_ID, followeeId)
                                      .setParameter(JpaConst.JPQL_PARM_FOLLOWER_ID, followerId)
                                      .getSingleResult();

            // トランザクションの開始
            em.getTransaction().begin();
            // フォロー情報の削除
            em.remove(followship);
            // トランザクションのコミット
            em.getTransaction().commit();
}

    /**
     * idを条件にデータを1件取得し、Employeeのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    private Followship findOneInternal(int id) {
        Followship f = em.find(Followship.class, id);
        return f;
    }

    /**
     * いいねデータを1件登録する
     * @param lv いいねデータ
     */
    private void createInternal(FollowshipView fv) {

        em.getTransaction().begin();
        em.persist(FollowshipConverter.toModel(fv));
        em.getTransaction().commit();

    }

    /**
     * 従業員データを更新する
     * @param ev 画面から入力された従業員の登録内容
     */
    private void update(FollowshipView fv) {
        em.getTransaction().begin();
        Followship f = findOneInternal(fv.getId());
        FollowshipConverter.copyViewToModel(f, fv);
        em.getTransaction().commit();
    }

    /**
     * 特定の従業員が特定の日報に「いいね」しているかどうかを確認する
     * @param employeeId 従業員ID
     * @param reportId 日報ID
     * @return 既に「いいね」している場合はtrue、そうでない場合はfalse
     */
    public boolean hasFollowed(int followeeId, int followerId) {
        long count = em.createNamedQuery(JpaConst.Q_FOL_COUNT_BY_FEE_AND_FER, Long.class)
                .setParameter(JpaConst.JPQL_PARM_FOLLOWEE_ID, followeeId)
                .setParameter(JpaConst.JPQL_PARM_FOLLOWER_ID, followerId)
                .getSingleResult();

        return count > 0;
    }

 // Employee -> EmployeeView の変換メソッド
    private EmployeeView toEmployeeView(Employee employee) {
        EmployeeView view = new EmployeeView();
        view.setId(employee.getId());
        view.setName(employee.getName());
        // 他の必要なフィールドを設定
        return view;
    }

}