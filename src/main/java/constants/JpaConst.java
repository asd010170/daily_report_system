package constants;

/**
 * DB関連の項目値を定義するインターフェース
 * ※インターフェイスに定義した変数は public static final 修飾子がついているとみなされる
 */
public interface JpaConst {

  //persistence-unit名
    String PERSISTENCE_UNIT_NAME = "daily_report_system";

    //データ取得件数の最大値
    int ROW_PER_PAGE = 15; //1ページに表示するレコードの数

    //従業員テーブル
    String TABLE_EMP = "employees"; //テーブル名
    //従業員テーブルカラム
    String EMP_COL_ID = "id"; //id
    String EMP_COL_CODE = "code"; //社員番号
    String EMP_COL_NAME = "name"; //氏名
    String EMP_COL_PASS = "password"; //パスワード
    String EMP_COL_ADMIN_FLAG = "admin_flag"; //管理者権限
    String EMP_COL_CREATED_AT = "created_at"; //登録日時
    String EMP_COL_UPDATED_AT = "updated_at"; //更新日時
    String EMP_COL_DELETE_FLAG = "delete_flag"; //削除フラグ

    int ROLE_ADMIN = 1; //管理者権限ON(管理者)
    int ROLE_GENERAL = 0; //管理者権限OFF(一般)
    int EMP_DEL_TRUE = 1; //削除フラグON(削除済み)
    int EMP_DEL_FALSE = 0; //削除フラグOFF(現役)

    //日報テーブル
    String TABLE_REP = "reports"; //テーブル名
    //日報テーブルカラム
    String REP_COL_ID = "id"; //id
    String REP_COL_EMP = "employee_id"; //日報を作成した従業員のid
    String REP_COL_REP_DATE = "report_date"; //いつの日報かを示す日付
    String REP_COL_TITLE = "title"; //日報のタイトル
    String REP_COL_CONTENT = "content"; //日報の内容
    String REP_COL_CREATED_AT = "created_at"; //登録日時
    String REP_COL_UPDATED_AT = "updated_at"; //更新日時

    //いいねテーブル
    String TABLE_LIK = "likes"; //テーブル名
    //いいねテーブルカラム
    String LIK_COL_ID = "id"; //id
    String LIK_COL_EMP = "employee_id"; //日報を作成した従業員のid
    String LIK_COL_REP = "report_id"; //日報id
    String LIK_COL_CREATED_AT = "created_at"; //登録日時

    //フォローシップテーブル
    String TABLE_FOL = "followship"; //テーブル名
    //いいねテーブルカラム
    String FOL_COL_ID = "id"; //id
    String FOL_COL_FEE = "followee_id"; //日報を作成した従業員のid
    String FOL_COL_FER = "follower_id"; //日報id
    String FOL_COL_CREATED_AT = "created_at"; //登録日時
    String FOL_COL_UPDATED_AT = "updated_at"; //更新日時

    //Entity名
    String ENTITY_EMP = "employee"; //従業員
    String ENTITY_REP = "report"; //日報
    String ENTITY_LIK = "liked"; //いいね
    String ENTITY_FOL = "followship"; // フォローシップ

    //JPQL内パラメータ
    String JPQL_PARM_CODE = "code"; //社員番号
    String JPQL_PARM_PASSWORD = "password"; //パスワード
    String JPQL_PARM_EMPLOYEE = "employee"; //従業員
    String JPQL_PARM_REPORT_ID = "reportId"; // 日報ID
    String JPQL_PARM_FOLLOWEE_ID = "followeeId"; // 日報ID
    String JPQL_PARM_FOLLOWER_ID = "followerId"; // 日報ID

    //NamedQueryの nameとquery
    //全ての従業員をidの降順に取得する
    String Q_EMP_GET_ALL = ENTITY_EMP + ".getAll"; //name
    String Q_EMP_GET_ALL_DEF = "SELECT e FROM Employee AS e ORDER BY e.id DESC"; //query
    //全ての従業員の件数を取得する
    String Q_EMP_COUNT = ENTITY_EMP + ".count";
    String Q_EMP_COUNT_DEF = "SELECT COUNT(e) FROM Employee AS e";
    //社員番号とハッシュ化済パスワードを条件に未削除の従業員を取得する
    String Q_EMP_GET_BY_CODE_AND_PASS = ENTITY_EMP + ".getByCodeAndPass";
    String Q_EMP_GET_BY_CODE_AND_PASS_DEF = "SELECT e FROM Employee AS e WHERE e.deleteFlag = 0 AND e.code = :" + JPQL_PARM_CODE + " AND e.password = :" + JPQL_PARM_PASSWORD;
    //指定した社員番号を保持する従業員の件数を取得する
    String Q_EMP_COUNT_REGISTERED_BY_CODE = ENTITY_EMP + ".countRegisteredByCode";
    String Q_EMP_COUNT_REGISTERED_BY_CODE_DEF = "SELECT COUNT(e) FROM Employee AS e WHERE e.code = :" + JPQL_PARM_CODE;
    //全ての日報をidの降順に取得する
    String Q_REP_GET_ALL = ENTITY_REP + ".getAll";
    String Q_REP_GET_ALL_DEF = "SELECT r FROM Report AS r ORDER BY r.id DESC";
    //全ての日報の件数を取得する
    String Q_REP_COUNT = ENTITY_REP + ".count";
    String Q_REP_COUNT_DEF = "SELECT COUNT(r) FROM Report AS r";
    //指定した従業員が作成した日報を全件idの降順で取得する
    String Q_REP_GET_ALL_MINE = ENTITY_REP + ".getAllMine";
    String Q_REP_GET_ALL_MINE_DEF = "SELECT r FROM Report AS r WHERE r.employee = :" + JPQL_PARM_EMPLOYEE + " ORDER BY r.id DESC";
    //指定した従業員が作成した日報の件数を取得する
    String Q_REP_COUNT_ALL_MINE = ENTITY_REP + ".countAllMine";
    String Q_REP_COUNT_ALL_MINE_DEF = "SELECT COUNT(r) FROM Report AS r WHERE r.employee = :" + JPQL_PARM_EMPLOYEE;
    //指定した従業員が作成した最新日報を取得する
    String Q_REP_GET_LATEST = ENTITY_REP + ".getLatest";
    String Q_REP_GET_LATEST_DEF = "SELECT r FROM Report AS r WHERE r.employee = :" + JPQL_PARM_EMPLOYEE + " ORDER BY r.reportDate DESC";
    //特定日報のいいね件数を取得する
    String Q_LIK_COUNT_BY_REPORT = ENTITY_LIK + ".countByReport";
    String Q_LIK_COUNT_BY_REPORT_DEF = "SELECT COUNT(l) FROM Liked AS l WHERE l.report.id = :" + JPQL_PARM_REPORT_ID;
    //特定日報のいいねを押したユーザーを取得する
    String Q_LIK_GET_USERS_BY_REPORT = ENTITY_LIK + ".getUsersByReport";
    String Q_LIK_GET_USERS_BY_REPORT_DEF = "SELECT l.employee.id FROM Liked AS l WHERE l.report.id = :" + JPQL_PARM_REPORT_ID;
    // 従業員IDと日報IDを条件に Liked エンティティの件数をカウントする
    String Q_LIK_COUNT_BY_EMP_AND_REP = ENTITY_LIK + ".countByEmployeeAndReport";
    String Q_LIK_COUNT_BY_EMP_AND_REP_DEF = "SELECT COUNT(l) FROM Liked AS l WHERE l.employee.id = :" + JPQL_PARM_EMPLOYEE + " AND l.report.id = :" + JPQL_PARM_REPORT_ID;
    //指定した従業員のフォロイー数を取得する
    String Q_FOL_COUNT_ALL_FEE = ENTITY_FOL + ".countAllFee";
    String Q_FOL_COUNT_ALL_FEE_DEF = "SELECT COUNT(f) FROM Followship AS f WHERE f.followee.id = :" + JPQL_PARM_FOLLOWEE_ID;
    //指定した従業員のフォロワー数を取得する
    String Q_FOL_COUNT_ALL_FER = ENTITY_FOL + ".countAllFer";
    String Q_FOL_COUNT_ALL_FER_DEF = "SELECT COUNT(f) FROM Followship AS f WHERE f.follower.id = :" + JPQL_PARM_FOLLOWER_ID;
    //フォロイーユーザーを取得する
    String Q_FOL_GET_USERS_BY_FEE = ENTITY_FOL + ".getUsersByFee";
    String Q_FOL_GET_USERS_BY_FEE_DEF = "SELECT f.follower.id FROM Followship AS f WHERE f.followee.id = :" + JPQL_PARM_FOLLOWEE_ID;
    //フォロワーユーザーを取得する
    String Q_FOL_GET_USERS_BY_FER = ENTITY_FOL + ".getUsersByFer";
    String Q_FOL_GET_USERS_BY_FER_DEF = "SELECT f.followee.id FROM Followship AS f WHERE f.follower.id = :" + JPQL_PARM_FOLLOWER_ID;
    // フォロイーIDとフォロワーIDを条件に Followship エンティティの件数をカウントする
    String Q_FOL_COUNT_BY_FEE_AND_FER = ENTITY_FOL + ".countByFeeAndFer";
    String Q_FOL_COUNT_BY_FEE_AND_FER_DEF = "SELECT COUNT(f) FROM Followship AS f WHERE f.followee.id = :" + JPQL_PARM_FOLLOWEE_ID + " AND f.follower.id = :" + JPQL_PARM_FOLLOWER_ID;
    // フォロイーIDとフォロワーIDを条件に Followship エンティティを取得する
    String Q_FOL_GET_BY_FEE_AND_FER = ENTITY_FOL + ".getByFeeAndFer";
    String Q_FOL_GET_BY_FEE_AND_FER_DEF = "SELECT f FROM Followship AS f WHERE f.followee.id = :" + JPQL_PARM_FOLLOWEE_ID + " AND f.follower.id = :" + JPQL_PARM_FOLLOWER_ID;
}
