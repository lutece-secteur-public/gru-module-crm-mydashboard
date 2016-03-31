/*
 * Copyright (c) 2002-2015, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.crm.modules.mydashboard.web;

import fr.paris.lutece.plugins.crm.business.demand.Demand;
import fr.paris.lutece.plugins.crm.business.demand.DemandFilter;
import fr.paris.lutece.plugins.crm.business.demand.DemandStatusCRM;
import fr.paris.lutece.plugins.crm.business.demand.DemandType;
import fr.paris.lutece.plugins.crm.business.demand.PaginationFilterSortManager;
import fr.paris.lutece.plugins.crm.business.user.CRMUser;
import fr.paris.lutece.plugins.crm.service.category.CategoryService;
import fr.paris.lutece.plugins.crm.service.demand.DemandService;
import fr.paris.lutece.plugins.crm.service.demand.DemandStatusCRMService;
import fr.paris.lutece.plugins.crm.service.demand.DemandTypeService;
import fr.paris.lutece.plugins.crm.service.parameters.AdvancedParametersService;
import fr.paris.lutece.plugins.crm.service.user.CRMUserService;
import fr.paris.lutece.plugins.crm.util.ListUtils;
import fr.paris.lutece.plugins.crm.util.constants.CRMConstants;
import fr.paris.lutece.plugins.mydashboard.service.MyDashboardComponent;
import fr.paris.lutece.plugins.mydashboard.web.MyDashboardApp;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.IPaginator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * MyDashboardCRMComponent
 */
public class MyDashboardCRMComponent extends MyDashboardComponent
{
    private static final String DASHBOARD_COMPONENT_ID = "crm-mydashboard.demandsComponent";
    private static final String MESSAGE_DASHBOARD_COMPONENT_DESCRIPTION = "module.crm.mydashboard.component.demands.description";
    private static final String TEMPLATE_DASHBOARD_COMPONENT = "skin/plugins/crm/modules/mydashboard/demands_component.html";
    private static final String MARK_XPAGE_MYDASHBOARD = "mydashboard";
  
    
    
    @Override
    public String getDashboardData( HttpServletRequest request )  
    {
        LuteceUser user = SecurityService.getInstance().getRegisteredUser(request);
        CRMUserService crmUserService = CRMUserService.getService(  );

        CRMUser crmUser = crmUserService.findByUserGuid( user.getName() );
        
        if( crmUser != null )
        {
        	 Map<String, Object> model = new HashMap<String, Object>(  );
             if ( crmUser != null )
             {
     	           //research by filter
     	            DemandFilter dFilter = new DemandFilter(  );
     	            dFilter.setIdCRMUser( crmUser.getIdCRMUser(  ) );
     	
     	            PaginationFilterSortManager paginationFilterSortManager = new PaginationFilterSortManager( request,MARK_XPAGE_MYDASHBOARD);
     	
     	            String strSession = (String) ( request.getParameter( CRMConstants.PARAMETER_SESSION ) );
     	
     	            if ( StringUtils.isBlank( strSession ) )
     	            {
     	                paginationFilterSortManager.cleanSession(  );
     	            }
     	
     	            String strIdStatusToSort = (String) ( request.getParameter( CRMConstants.PARAMETER_ID_STATUS ) );
     	            String strSortField = (String) ( request.getParameter( CRMConstants.PARAMETER_SORT_ATTRIBUTE ) );
     	            String strSortOrder = (String) ( request.getParameter( CRMConstants.PARAMETER_SORT_ORDER ) );
     	
     	            int nIdStatusToSort = -1;
     	
     	            if ( StringUtils.isNotEmpty( strIdStatusToSort ) )
     	            {
     	                nIdStatusToSort = Integer.parseInt( strIdStatusToSort );
     	            }
     	
     	            if ( StringUtils.isNotEmpty( strSortField ) && StringUtils.isNotEmpty( strSortOrder ) )
     	            {
     	                paginationFilterSortManager.storeSort( nIdStatusToSort, strSortField,
     	                    Boolean.parseBoolean( strSortOrder ) );
     	            }
     	
     	            String strModificationDate = request.getParameter( CRMConstants.PARAMETER_MODIFICATIONDATE );
     	            String strDemandType = request.getParameter( CRMConstants.PARAMETER_DEMANDTYPE );
     	            String strNotification = request.getParameter( CRMConstants.PARAMETER_NOTIFICATION );
     	
     	            if ( StringUtils.isNotBlank( strModificationDate ) || StringUtils.isNotBlank( strDemandType ) ||
     	                    StringUtils.isNotBlank( strNotification ) )
     	            {
     	                paginationFilterSortManager.cleanSessionFilter(  );
     	            }
     	
     	            if ( StringUtils.isNotBlank( strModificationDate ) )
     	            {
     	                Date modificationDate = checkFormatModificationDateFilter( strModificationDate, request );
     	                paginationFilterSortManager.storeFilterModificationDate( modificationDate );
     	                paginationFilterSortManager.storeFilterStringModificationDate( strModificationDate );
     	            }
     	
     	            if ( StringUtils.isNotBlank( strDemandType ) )
     	            {
     	                int nIdDemandType = Integer.parseInt( strDemandType );
     	                paginationFilterSortManager.storeFilterDemandType( nIdDemandType );
     	            }
     	
     	            if ( StringUtils.isNotBlank( strNotification ) )
     	            {
     	                paginationFilterSortManager.storeFilterNotification( strNotification );
     	            }
     	
     	            Date dateModificationSession = paginationFilterSortManager.retrieveFilterModificationDate(  );
     	
     	            if ( dateModificationSession != null )
     	            {
     	                dFilter.setDateModification( dateModificationSession );
     	                model.put( CRMConstants.MARK_MODIFICATIONDATE,
     	                    paginationFilterSortManager.retrieveFilterStringModificationDate(  ) );
     	            }
     	
     	            Integer nIdDemandTypeSession = paginationFilterSortManager.retrieveFilterDemandType(  );
     	
     	            if ( ( nIdDemandTypeSession != null ) && ( nIdDemandTypeSession >= 0 ) )
     	            {
     	                dFilter.setIdDemandType( nIdDemandTypeSession );
     	            }
     	
     	            String strNotificationSession = paginationFilterSortManager.retrieveFilterNotification(  );
     	
     	            if ( StringUtils.isNotBlank( strNotificationSession ) )
     	            {
     	                dFilter.setNotification( strNotificationSession );
     	            }
     	            
     	            model.put( CRMConstants.MARK_MAP_DEMANDS_LIST,
     	                    DemandService.getService().findByFilterMap( dFilter, request.getLocale(  ), paginationFilterSortManager ) );
     	            model.put( CRMConstants.MARK_FILTER, dFilter );
     	            
     	            Map<String, IPaginator<Demand>> mapPaginator = new HashMap<String, IPaginator<Demand>>(  );
     	            Map<String, String> mapNbItemsPerPage = new HashMap<String, String>(  );
     	            int nIdStatus;
     	
     	            for ( DemandStatusCRM statusCRM : DemandStatusCRMService.getService(  )
     	                                                                    .getAllStatusCRM( request.getLocale(  ) ) )
     	            {
     	                nIdStatus = statusCRM.getIdStatusCRM(  );
     	
     	                IPaginator<Demand> paginator = paginationFilterSortManager.retrievePaginator( nIdStatus );
     	                int nItemsPerPage = paginationFilterSortManager.retrieveItemsPerPage( nIdStatus );
     	
     	                mapNbItemsPerPage.put( Integer.toString( nIdStatus ), Integer.toString( nItemsPerPage ) );
     	                mapPaginator.put( Integer.toString( nIdStatus ), paginator );
     	            }
     	            model.put( CRMConstants.MARK_STATUS_CRM_LIST, DemandStatusCRMService.getService(  ).getAllStatusCRM( request.getLocale(  ) ) );
     	
     	            model.put( CRMConstants.MARK_MAP_PAGINATOR, mapPaginator );
     	            model.put( CRMConstants.MARK_MAP_NB_ITEMS_PER_PAGE, mapNbItemsPerPage );
     	            model.put( CRMConstants.MARK_DISPLAYDRAFT,
     	            		AdvancedParametersService.getService(  ).isParameterValueByKey( CRMConstants.CONSTANT_DISPLAYDRAFT ) );
     	             
             	}
             	model.put( CRMConstants.MARK_LOCALE, request.getLocale(  ) );
                 model.put( CRMConstants.MARK_MAP_DEMAND_TYPES_LIST, DemandTypeService.getService().findForLuteceUser( request ) );
                 model.put( CRMConstants.MARK_CATEGORIES_LIST,
                     CategoryService.getService().getCategories( request.getLocale(  ), false, true ) );
                  model.put( CRMConstants.MARK_DEMAND_TYPES_LIST, DemandTypeService.getService().findAll(  ) );
               
                 model.put( CRMConstants.MARK_CRM_USER, crmUser );
                 List<DemandType> listAllOpenedDemandType = initListAllOpenedDemandType(  );

                 model.put( CRMConstants.MARK_DEMAND_TYPES_REFLIST,
                     ListUtils.toReferenceList( listAllOpenedDemandType, "idDemandType", "label", "" ) );
                 
                 model.put(CRMConstants.MARK_MAP_DO_LOGIN,SecurityService.getInstance(  ).getLoginPageUrl() );
                 model.put(CRMConstants.MARK_BASE_URL, AppPathService.getBaseUrl(request));
            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_DASHBOARD_COMPONENT,
                    LocaleService.getDefault(  ), model );

            return template.getHtml(  );
        }
        return "";
    }
    
    /**
     * Get the list of all the opened demand types only
     * @return the list of opened demand types
     */
    private List<DemandType> initListAllOpenedDemandType(  )
    {
        List<DemandType> listAllDemandType = DemandTypeService.getService(  ).findAll(  );
        List<DemandType> listAllOpenedDemandType = new ArrayList<DemandType>(  );

        for ( DemandType demandType : listAllDemandType )
        {
            if ( demandType.isOpen(  ) )
            {
                listAllOpenedDemandType.add( demandType );
            }
        }

        return listAllOpenedDemandType;
    }
    /**
     * Check the format of the filter modification date
     * 
     */
    private Date checkFormatModificationDateFilter( String strModificationDate, HttpServletRequest request )
        
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" );
        sdf.setLenient( true );

        Date d = new Date(  );

        try
        {
            d = sdf.parse( strModificationDate );
        }
        catch ( Exception e )
        {
           AppLogService.error(e);
        }

        
        return d;
    }

    @Override
    public String getComponentId(  )
    {
        return DASHBOARD_COMPONENT_ID;
    }

    @Override
    public String getComponentDescription( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_DASHBOARD_COMPONENT_DESCRIPTION, locale );
    }
    
    
    
}
