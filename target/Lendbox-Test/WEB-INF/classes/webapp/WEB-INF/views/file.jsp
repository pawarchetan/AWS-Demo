<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<html>
<head>
	<title>LendBox-test</title>
	<style type="text/css">
		.tg  {border-collapse:collapse;border-spacing:0;border-color:#ccc;}
		.tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#fff;}
		.tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#f0f0f0;}
		.tg .tg-4eph{background-color:#f9f9f9}
	</style>
</head>
<body>
<h1>
	File Upload
</h1>
<form action="/file/upload" method="post" enctype="multipart/form-data">
    Select a File : <input type="file" name="file" size="50"/>
    <input type="submit" value="upload"/>
</form>
<tr><td colspan="2" style="color: red;"><form:errors path="*" cssStyle="color : red;"/>
    ${errors}
</td></tr>

<br>
<h3>File List</h3>
<c:if test="${!empty listFiles}">
	<table class="tg">
	<tr>
		<th width="30">ID</th>
		<th width="120">Name</th>
        <th width="250">URL</th>
        <th width="60">Size</th>
        <th width="70">Uploaded Date</th>
	</tr>
	<c:forEach items="${listFiles}" var="file">
		<tr>
			<td>${file.id}</td>
			<td>${file.name}</td>
            <td>${file.url}</td>
            <td>${file.size}</td>
            <td>${file.createdDate}</td>
		</tr>
	</c:forEach>
	</table>
</c:if>
</body>
</html>
