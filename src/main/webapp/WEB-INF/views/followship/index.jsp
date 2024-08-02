<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.ForwardConst" %>

<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commFol" value="${ForwardConst.CMD_FOLLOW.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">

        <h2>フォロー一覧 詳細ページ</h2>

        <table id="report_list">
            <tbody>
                <tr>
                    <th class="report_name">氏名</th>
                    <th class="report_date">更新日</th>
                    <th class="report_title">最新投稿（タイトル）</th>
                    <th class="report_action">操作</th>
                    <th class="report_liked">いいね！</th>
                </tr>
                <c:forEach var="followee" items="${followee}" varStatus="status">
                    <c:set var="report" value="${reports[status.index]}" />
                    <fmt:parseDate value="${report.reportDate}" pattern="yyyy-MM-dd" var="reportDay" type="date" />

                    <tr>
                        <td class="report_name"><c:out value="${followee.name}" /></td>
                        <td class="report_date"><fmt:formatDate value='${reportDay}' pattern='yyyy-MM-dd' /></td>
                        <td class="report_title"><c:out value="${report.title}" /></td>
                        <td class="report_action"><a href="<c:url value='?action=${actRep}&command=${commShow}&id=${report.id}' />">詳細を見る</a></td>
                        <td class="report_liked"><c:out value="${likes_count[report.id]}" /></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <p>
            <a href="<c:url value='?action=${actRep}&command=${commIdx}' />">一覧に戻る</a>
        </p>
    </c:param>
</c:import>