<div class="container">
    <div class="row">
   
        <#assign layoutLocalService = serviceLocator.findService("com.liferay.portal.kernel.service.LayoutLocalService")>
        <#assign dlAppLocalServiceUtil = staticUtil["com.liferay.document.library.kernel.service.DLAppLocalServiceUtil"] >
        <#assign dlUtil = staticUtil["com.liferay.document.library.kernel.util.DLUtil"] >		  
        <#assign GroupLocalService = serviceLocator.findService("com.liferay.portal.kernel.service.GroupLocalService")>
        
        <#if entries?has_content>
        
    	        <#list entries as curEntry>
    	        
    	         <div class="col-sm-4">
						<#assign
								assetRenderer = curEntry.getAssetRenderer()
								journalArticle = assetRenderer.getAssetObject()
						 />
						 
						<!-- get field values for entry -->
						<#assign fields = curEntry.getAssetRenderer().getDDMFormValuesReader().getDDMFormValues().getDDMFormFieldValues()/>
				
						<#assign slideImage = "">
						<#assign Name = "">
						<#assign Straße = "">
						<#assign Ort = "">
						
						<#assign siteFriendlyURL = "">
						<#assign entryGroup = GroupLocalService.fetchGroup(curEntry.getGroupId())>
						<#assign groupFriendlyURL = entryGroup.getFriendlyURL()>
						
						<#if entryGroup.hasPublicLayouts()>
								<#assign siteFriendlyURL = "/web" + groupFriendlyURL>
						<#else>
								<#assign siteFriendlyURL = "/group" + groupFriendlyURL>
						</#if>
						
						<#list fields as field>
							<#if field.getName() == "Bild22pt">
								<#assign slideImage = getDocumentURL(field)>
							</#if>
							<#if field.getName() == "Name">
								<#assign Name = field.getValue().getString(locale)>
							</#if>
							<#if field.getName() == "Straße">
								<#assign Straße = field.getValue().getString(locale)>
							</#if>
							<#if field.getName() == "Ort">
								<#assign Ort = field.getValue().getString(locale)>
							</#if>
						</#list>
						
						<div class="row">
							<div class="col-sm-6">
									<#if slideImage?has_content>
										<img src="${slideImage}"  class="autofit-row rounded-lg">
									</#if>
							</div>
							<div class="col-sm-6">
									<#if Name?has_content>
											<div>${Name}</div>
									</#if>
									<#if Bild?has_content>
											<div>${Bild}</div>
									</#if>
									<#if Straße?has_content>
											<div>${Straße}</div>
									 </#if>
									 <#if Ort?has_content>
											<div>${Ort}</div>
									</#if>
									<#if siteFriendlyURL?has_content>
									    <div>
									        <br>
											<a href="${siteFriendlyURL}" target="_blank">View Site</a>
										</div>	
									</#if>
							</div>
						</div>
    		    </div>
    	    </#list>
        </#if>
    </div>
</div>

<#function getDocumentURL field>
    <#assign imageJson = field.getValue().getString(locale)>
    <#if imageJson?has_content>
        <#assign imageJson = jsonFactoryUtil.createJSONObject(imageJson)
        uuid = imageJson.uuid
        groupId = imageJson.groupId
        dlFile = dlAppLocalServiceUtil.getFileEntryByUuidAndGroupId(uuid,groupId?number)
        smallImage =  dlUtil.getPreviewURL(dlFile, dlFile.getFileVersion(),themeDisplay,'')
        />
        <#return smallImage>
    </#if>
    <#return ''>
</#function>