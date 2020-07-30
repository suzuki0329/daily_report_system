package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsLikeServlet
 */
@WebServlet("/reports/like")
public class ReportsLikeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsLikeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            EntityManager em = DBUtil.createEntityManager();


            // find で Report を検索(Reportのidを取得)
            /* show.jsp <c:url value="/reports/like?id=${report.「id」←
             * ←request.getParameter(「"id"」)ココの文字が一致しないといけない!  }" />">この日報にいいねする
             */
            Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));

            // like_count をゲッターで現在のカウント数を取得し+1する
            Integer like_count = r.getLike_count();
            if (like_count == null) {
                like_count = 1;
            } else {
                like_count++;
            }

            // セッターで格納
            r.setLike_count(like_count);


            // データベースを更新
            em.getTransaction().begin();
            em.persist(r);
            em.getTransaction().commit();
            em.close();

            // indexページへリダイレクト
            request.getSession().setAttribute("flush", "いいねしました。");

            request.getSession().removeAttribute("report_id");

            response.sendRedirect(request.getContextPath() + "/reports/index");


    }

}
