<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.ForwardConst" %>

<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commEdt" value="${ForwardConst.CMD_EDIT.getValue()}" />
<c:set var="commFol" value="${ForwardConst.CMD_FOLLOW.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">

        <h2>マイページ 詳細ページ</h2>

            <c:if test="${errors != null}">
        <div id="flush_error">
        <c:forEach var="error" items="${errors}">
            <c:out value="${error}" /><br />
        </c:forEach>
        </div>
         </c:if>

        <table>
            <tbody>
                <tr>
                    <th>氏名</th>
                    <td><c:out value="${employee.name}" /></td>
                </tr>
                <tr>
                    <th>フォロー中人数</th>
                    <td><c:out value="${fee_count} 人" /></td>
                </tr>
                <tr>
                    <th>フォロワー人数</th>
                    <td><c:out value="${fer_count} 人" /></td>
                </tr>
                <tr>
                    <th>フォロー中</th>
                    <td><c:out value="${fee_who}" /></td>
                </tr>
                <tr>
                    <th>フォロワー</th>
                    <td><c:out value="${fer_who}" /></td>
                </tr>
                <tr>
                    <th>日報投稿数</th>
                    <td><c:out value="${reports_count} 件" /></td>
                </tr>
            </tbody>
        </table>

        <c:if test="${!(sessionScope.login_employee.id == employee.id)}">
            <form method="POST" action="<c:url value='?action=${actRep}&command=${commFol}&id=${employee.id}' />">
            <input type="hidden" name="${AttributeConst.LOGIN_EMP.getValue()}" value="${sessionScope.login_employee.id}" />
            <input type="hidden" name="${AttributeConst.EMP_ID.getValue()}" value="${employee.id}" />
                <div class="follow_button">
                    <c:choose>
                        <c:when test="${hasfollowed != true}">
                    <button type="submit">フォロー済み</button>
                    </c:when>
                        <c:otherwise>
                    <button type="submit">フォロー</button>
                </c:otherwise>
            </c:choose>
                </div>
        </form>
        </c:if>

        <p>
            <a href="<c:url value='?action=${actRep}&command=${commIdx}' />">一覧に戻る</a>
        </p>
    </c:param>
</c:import>