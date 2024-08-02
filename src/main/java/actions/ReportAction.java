package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.FollowshipView;
import actions.views.LikedView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.EmployeeService;
import services.FollowshipService;
import services.LikedService;
import services.ReportService;

/**
 * 日報に関する処理を行うActionクラス
 *
 */
public class ReportAction extends ActionBase {

    private ReportService service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new ReportService();

        //メソッドを実行
        invoke();
        service.close();
    }

    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException, IOException {

        //指定されたページ数の一覧画面に表示する日報データを取得
        int page = getPage();
        List<ReportView> reports = service.getAllPerPage(page);

        //全日報データの件数を取得
        long reportsCount = service.countAll();

        putRequestScope(AttributeConst.REPORTS, reports); //取得した日報データ
        putRequestScope(AttributeConst.REP_COUNT, reportsCount); //全ての日報データの件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

        //日報Idを指定し、いいねデータの件数を取得
        LikedService serviceL = new LikedService();
        HashMap<Integer, Long> likesCountMap = new HashMap<>();

        for (int i = 1; i <= reportsCount; i++) {
        ReportView rv = service.findOne(i);
        long likescount = serviceL.countAllRep(i);

        // HashMapにキーと値を追加
        likesCountMap.put(rv.getId(), likescount);
        }

        putRequestScope(AttributeConst.LIK_COUNT, likesCountMap);

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_REP_INDEX);
    }


/**
 * 新規登録画面を表示する
 * @throws ServletException
 * @throws IOException
 */
public void entryNew() throws ServletException, IOException {

    putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン

    //日報情報の空インスタンスに、日報の日付＝今日の日付を設定する
    ReportView rv = new ReportView();
    rv.setReportDate(LocalDate.now());
    putRequestScope(AttributeConst.REPORT, rv); //日付のみ設定済みの日報インスタンス

    //新規登録画面を表示
    forward(ForwardConst.FW_REP_NEW);

}

/**
 * 新規登録を行う
 * @throws ServletException
 * @throws IOException
 */
public void create() throws ServletException, IOException {

    //CSRF対策 tokenのチェック
    if (checkToken()) {

        //日報の日付が入力されていなければ、今日の日付を設定
        LocalDate day = null;
        if (getRequestParam(AttributeConst.REP_DATE) == null
                || getRequestParam(AttributeConst.REP_DATE).equals("")) {
            day = LocalDate.now();
        } else {
            day = LocalDate.parse(getRequestParam(AttributeConst.REP_DATE));
        }

        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        //パラメータの値をもとに日報情報のインスタンスを作成する
        ReportView rv = new ReportView(
                null,
                ev, //ログインしている従業員を、日報作成者として登録する
                day,
                getRequestParam(AttributeConst.REP_TITLE),
                getRequestParam(AttributeConst.REP_CONTENT),
                null,
                null);

        //日報情報登録
        List<String> errors = service.create(rv);

        if (errors.size() > 0) {
            //登録中にエラーがあった場合

            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
            putRequestScope(AttributeConst.REPORT, rv);//入力された日報情報
            putRequestScope(AttributeConst.ERR, errors);//エラーのリスト

            //新規登録画面を再表示
            forward(ForwardConst.FW_REP_NEW);

        } else {
            //登録中にエラーがなかった場合

            //セッションに登録完了のフラッシュメッセージを設定
            putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

            //一覧画面にリダイレクト
            redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);
        }
    }
}

/**
 * 詳細画面を表示する
 * @throws ServletException
 * @throws IOException
 */
public void show() throws ServletException, IOException {

    ReportService serviceR = new ReportService();
    LikedService service = new LikedService();

    //idを条件に日報データを取得する
    ReportView rv = serviceR.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));
    //日報Idを指定し、いいねデータの件数を取得
    long likescount = service.countAllRep(toNumber(getRequestParam(AttributeConst.REP_ID)));
    String showWhoLiked = service.showAllRep(toNumber(getRequestParam(AttributeConst.REP_ID)));


    if (rv == null) {
        //該当の日報データが存在しない場合はエラー画面を表示
        forward(ForwardConst.FW_ERR_UNKNOWN);

    } else {

        putRequestScope(AttributeConst.REPORT, rv); //取得した日報データ
        putRequestScope(AttributeConst.LIK_COUNT, likescount);
        putRequestScope(AttributeConst.LIK_WHO, showWhoLiked);

        //詳細画面を表示
        forward(ForwardConst.FW_REP_SHOW);
    }
}

/**
 * 編集画面を表示する
 * @throws ServletException
 * @throws IOException
 */
public void edit() throws ServletException, IOException {

    //idを条件に日報データを取得する
    ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

    //セッションからログイン中の従業員情報を取得
    EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

    if (rv == null || ev.getId() != rv.getEmployee().getId()) {
        //該当の日報データが存在しない、または
        //ログインしている従業員が日報の作成者でない場合はエラー画面を表示
        forward(ForwardConst.FW_ERR_UNKNOWN);

    } else {

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
        putRequestScope(AttributeConst.REPORT, rv); //取得した日報データ

        //編集画面を表示
        forward(ForwardConst.FW_REP_EDIT);
    }

}

/**
 * 更新を行う
 * @throws ServletException
 * @throws IOException
 */
public void update() throws ServletException, IOException {

    //CSRF対策 tokenのチェック
    if (checkToken()) {

        //idを条件に日報データを取得する
        ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

        //入力された日報内容を設定する
        rv.setReportDate(toLocalDate(getRequestParam(AttributeConst.REP_DATE)));
        rv.setTitle(getRequestParam(AttributeConst.REP_TITLE));
        rv.setContent(getRequestParam(AttributeConst.REP_CONTENT));

        //日報データを更新する
        List<String> errors = service.update(rv);

        if (errors.size() > 0) {
            //更新中にエラーが発生した場合

            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
            putRequestScope(AttributeConst.REPORT, rv); //入力された日報情報
            putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

            //編集画面を再表示
            forward(ForwardConst.FW_REP_EDIT);
        } else {
            //更新中にエラーがなかった場合

            //セッションに更新完了のフラッシュメッセージを設定
            putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

            //一覧画面にリダイレクト
            redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);

        }
    }
}

/**
 * いいね！を登録する
 * @throws ServletException
 * @throws IOException
 */
public void liked() throws ServletException, IOException {

    ReportService serviceR = new ReportService();
    LikedService service = new LikedService();

    // セッションからログイン中の従業員情報を取得
    EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
    // idを条件に日報データを取得する
    ReportView rv = serviceR.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

    // 既に同じ従業員が同じ日報に「いいね」しているか確認する
    boolean hasLiked = service.hasLiked(ev.getId(), rv.getId());

    if (hasLiked) {
        // 既に「いいね」している場合、エラーメッセージを設定する
        List<String> errors = new ArrayList<>();
        errors.add("この投稿には既に「いいね」しています。");

        // エラー情報と日報情報をリクエストスコープに設定
        putRequestScope(AttributeConst.REPORT, rv); // 入力された日報情報
        putRequestScope(AttributeConst.ERR, errors); // エラーのリスト

        // いいねデータの件数を取得して設定
        long likescount = service.countAllRep(toNumber(getRequestParam(AttributeConst.REP_ID)));
        String showWhoLiked = service.showAllRep(toNumber(getRequestParam(AttributeConst.REP_ID)));
        putRequestScope(AttributeConst.LIK_COUNT, likescount);
        putRequestScope(AttributeConst.LIK_WHO, showWhoLiked);

        // 新規登録画面を再表示
        forward(ForwardConst.FW_REP_SHOW);
        return;
    }

    // いいね情報のインスタンスを作成する
    LikedView lv = new LikedView(
            null,
            ev, // ログインしている従業員
            rv,
            null);

    // いいね情報登録
    List<String> errors = service.create(lv);

    // 日報Idを指定し、いいねデータの件数を取得
    long likescount = service.countAllRep(toNumber(getRequestParam(AttributeConst.REP_ID)));
    String showWhoLiked = service.showAllRep(toNumber(getRequestParam(AttributeConst.REP_ID)));

    if (errors.size() > 0) {
        // 登録中にエラーがあった場合
        putRequestScope(AttributeConst.REPORT, rv); // 入力された日報情報
        putRequestScope(AttributeConst.ERR, errors); // エラーのリスト
    } else {
        // 登録中にエラーがなかった場合
        putRequestScope(AttributeConst.REPORT, rv); // 取得した日報データ
    }

    putRequestScope(AttributeConst.LIK_COUNT, likescount);
    putRequestScope(AttributeConst.LIK_WHO, showWhoLiked);

    // 詳細画面を表示
    forward(ForwardConst.FW_REP_SHOW);
}

/**
 * MYPAGE画面を表示する
 * @throws ServletException
 * @throws IOException
 */
public void mypage() throws ServletException, IOException {

        EmployeeService serviceE = new EmployeeService();
        ReportService serviceR = new ReportService();
        FollowshipService service = new FollowshipService();

        //idを条件に従業員データを取得する
        EmployeeView ev = serviceE.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));
        // セッションからログイン中の従業員情報を取得
        EmployeeView erv = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        // 既に同じ従業員が同じ日報に「いいね」しているか確認する
        boolean hasFollowed = service.hasFollowed(ev.getId(), erv.getId());

        putRequestScope(AttributeConst.HASFOLLOWED, hasFollowed);
        putRequestScope(AttributeConst.EMPLOYEE, ev); //取得した従業員情報

        long ReportsCount = serviceR.countAllMine(ev);
        putRequestScope(AttributeConst.REP_COUNT, ReportsCount);

        // フォローシップデータの件数を取得して設定
        long followeecount = service.countAllFee(toNumber(getRequestParam(AttributeConst.REP_ID)));
        long followercount = service.countAllFer(toNumber(getRequestParam(AttributeConst.REP_ID)));
        String showWhoFee = service.showAllFee(toNumber(getRequestParam(AttributeConst.REP_ID)));
        String showWhoFer = service.showAllFer(toNumber(getRequestParam(AttributeConst.REP_ID)));
        putRequestScope(AttributeConst.FEE_COUNT, followeecount);
        putRequestScope(AttributeConst.FER_COUNT, followercount);
        putRequestScope(AttributeConst.FEE_WHO, showWhoFee);
        putRequestScope(AttributeConst.FER_WHO, showWhoFer);

        //MYPAGE画面を表示
        forward(ForwardConst.FW_MY_PAGE);
    }

/**
 * フォローを登録する
 * @throws ServletException
 * @throws IOException
 */
public void follow() throws ServletException, IOException {

    EmployeeService serviceE = new EmployeeService();
    ReportService serviceR = new ReportService();
    FollowshipService service = new FollowshipService();

    // セッションからログイン中の従業員情報を取得
    EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
    // idを条件に日報データを取得する
    EmployeeView erv = serviceE.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));

    // 既に同じ従業員が同じ日報に「いいね」しているか確認する
    boolean hasFollowed = service.hasFollowed(ev.getId(), erv.getId());

    if (hasFollowed) {
        // 既にフォローされている場合、フォローを解除する
        service.delete(ev.getId(), erv.getId());
    } else {
        // フォロー情報のインスタンスを作成する
        FollowshipView lv = new FollowshipView(
                null,
                ev, // ログインしている従業員
                erv,
                null,
                null);

        // フォロー情報を登録する
        List<String> errors = service.create(lv);

        if (errors.size() > 0) {
            // 登録中にエラーがあった場合
            putRequestScope(AttributeConst.EMPLOYEE, erv);
            putRequestScope(AttributeConst.ERR, errors);
        }
    }

    putRequestScope(AttributeConst.EMPLOYEE, erv); //取得した従業員情報
    putRequestScope(AttributeConst.HASFOLLOWED, hasFollowed);

    long ReportsCount = serviceR.countAllMine(erv);
    putRequestScope(AttributeConst.REP_COUNT, ReportsCount);

    // フォローシップデータの件数を取得して設定
    long followeecount = service.countAllFee(toNumber(getRequestParam(AttributeConst.REP_ID)));
    long followercount = service.countAllFer(toNumber(getRequestParam(AttributeConst.REP_ID)));
    String showWhoFee = service.showAllFee(toNumber(getRequestParam(AttributeConst.REP_ID)));
    String showWhoFer = service.showAllFer(toNumber(getRequestParam(AttributeConst.REP_ID)));
    putRequestScope(AttributeConst.FEE_COUNT, followeecount);
    putRequestScope(AttributeConst.FER_COUNT, followercount);
    putRequestScope(AttributeConst.FEE_WHO, showWhoFee);
    putRequestScope(AttributeConst.FER_WHO, showWhoFer);

    //MYPAGE画面を表示
    forward(ForwardConst.FW_MY_PAGE);
}

/**
 * フォロー一覧画面を表示する
 * @throws ServletException
 * @throws IOException
 */
public void followed() throws ServletException, IOException {

        LikedService serviceL = new LikedService();
        FollowshipService serviceF = new FollowshipService();
        ReportService service = new ReportService();

        //セッションからログイン中の従業員情報を取得
        EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
        List<EmployeeView> followees = serviceF.showAllFeeE(toNumber(getRequestParam(AttributeConst.EMP_ID)));
        // フォロイーの最新のレポートを取得
        List<ReportView> latestReports = service.findLatestOne(followees);

        //日報Idを指定し、いいねデータの件数を取得
        HashMap<Integer, Long> likesCountMap = new HashMap<>();
        for (ReportView rv : latestReports) {
            long likesCount = serviceL.countAllRep(rv.getId());
            // HashMapにキーと値を追加
            likesCountMap.put(rv.getId(), likesCount);
        }

        putRequestScope(AttributeConst.LIK_COUNT, likesCountMap);
        putRequestScope(AttributeConst.EMPLOYEE, loginEmployee);
        putRequestScope(AttributeConst.FOLLOWEE, followees);
        putRequestScope(AttributeConst.REPORTS, latestReports);

        //詳細画面を表示
        forward(ForwardConst.FW_FOL_INDEX);
    }
}