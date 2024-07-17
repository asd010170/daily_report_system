package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.LikedView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import services.LikedService;
import services.ReportService;

/**
 * 日報に関する処理を行うActionクラス
 *
 */
public class LikedAction extends ActionBase {

    private LikedService service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new LikedService();

        //メソッドを実行
        invoke();
        service.close();
    }

    /**
     * いいね！を登録する
     * @throws ServletException
     * @throws IOException
     */
    public void liked() throws ServletException, IOException {

        ReportService serviceR = new ReportService();

            // セッションからログイン中の従業員情報を取得
            EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
            // idを条件に日報データを取得する
            ReportView rv = serviceR.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

                // いいね情報のインスタンスを作成する
                LikedView lv = new LikedView(
                        null,
                        ev, // ログインしている従業員
                        rv,
                        null);

                // いいね情報登録
                List<String> errors = service.create(lv);

                if (errors.size() > 0) {
                    // 登録中にエラーがあった場合
                    putRequestScope(AttributeConst.REPORT, rv); // 入力された日報情報
                    putRequestScope(AttributeConst.ERR, errors); // エラーのリスト

                    // 新規登録画面を再表示
                    forward(ForwardConst.FW_REP_SHOW);

                } else {
                    // 登録中にエラーがなかった場合
                    putRequestScope(AttributeConst.REPORT, rv); //取得した日報データ

                    //詳細画面を表示
                    forward(ForwardConst.FW_REP_SHOW);
                }
            }
        }