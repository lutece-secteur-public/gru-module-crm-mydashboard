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

import fr.paris.lutece.plugins.crm.business.demand.DemandFilter;
import fr.paris.lutece.plugins.crm.business.user.CRMUser;
import fr.paris.lutece.plugins.crm.service.demand.DemandService;
import fr.paris.lutece.plugins.crm.service.user.CRMUserService;
import fr.paris.lutece.plugins.mydashboard.service.MyDashboardComponent;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.util.html.HtmlTemplate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * MyDashboardCRMComponent
 */
public class MyDashboardCRMComponent extends MyDashboardComponent
{
    private static final String DASHBOARD_COMPONENT_ID = "crm-mydashboard.demandsComponent";
    private static final String MESSAGE_DASHBOARD_COMPONENT_DESCRIPTION = "module.crm.mydashboard.component.demands.description";
    private static final String TEMPLATE_DASHBOARD_COMPONENT = "skin/plugins/crm/modules/mydashboard/demands_component.html";
    private static final String MARK_DEMANDS_LIST = "demands_list";
    
    
    @Override
    public String getDashboardData( HttpServletRequest request )
    {
        LuteceUser user = SecurityService.getInstance().getRegisteredUser(request);
        CRMUserService crmUserService = CRMUserService.getService(  );

        CRMUser crmUser = crmUserService.findByUserGuid( user.getName() );
        
        if( crmUser != null )
        {
            DemandService demandService = DemandService.getService(  );
            Map<String, Object> model = new HashMap<String, Object>(  );
            DemandFilter dFilter = new DemandFilter();
            dFilter.setIdCRMUser( crmUser.getIdCRMUser() );
            model.put( MARK_DEMANDS_LIST, demandService.findByFilter( dFilter ) );
            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_DASHBOARD_COMPONENT,
                    LocaleService.getDefault(  ), model );

            return template.getHtml(  );
        }
        return "";
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
