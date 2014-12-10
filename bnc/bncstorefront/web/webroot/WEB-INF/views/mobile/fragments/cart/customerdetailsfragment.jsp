<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="bnc" tagdir="/WEB-INF/tags/mobile/bnc_csr" %>
<json:object>
	<json:property name="customer_details" escapeXml="false">
		<bnc:customerDetailsButtons/>
		<bnc:customerDetailInformation/>
	</json:property>
</json:object>