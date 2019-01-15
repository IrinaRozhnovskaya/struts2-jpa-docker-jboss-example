<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>Chat</title>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/webjars/material-design-icons/3.0.1/material-icons.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/webjars/materializecss/1.0.0/css/materialize.min.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/styles.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.png" type="image/png">
</head>
<body>
<nav>
    <ul>
        <li><a href="${pageContext.request.contextPath}">Home</a></li>
    </ul>
</nav>

<div>
    <s:form cssClass="inline center-align">
        <s:textfield autofocus="autofocus"
                     id="from" name="from" type="text" placeholder="Type your name here"/>
        <s:textfield autofocus="autofocus"
                     id="body" name="body" type="text" placeholder="Type a message here"/>
        <s:submit cssClass="hide"/>
    </s:form>

    <ul class="collection left-align">
        <s:iterator value="messages" var="message">
            <li class="collection-item">
                    ${message.from}, ${message.createdAt}
                        <br/>  <div class="chip">${message.body}</div> <br>

            </li>
        </s:iterator>
    </ul>
</div>

<script type="text/javascript"
        src="${pageContext.request.contextPath}/webjars/materializecss/1.0.0/js/materialize.min.js"></script>
<script>M.AutoInit(document.body);</script>

</body>
</html>