/*
 * Copyright (c) 2002-2026, City of Paris
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

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.mydashboard.service.IMyDashboardComponent;
import fr.paris.lutece.test.LuteceTestCase;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

/**
 * Tests of the CRM demands dashboard component
 */
public class MyDashboardCRMComponentTest extends LuteceTestCase
{
    private static final String COMPONENT_ID = "crm-mydashboard.demandsComponent";
    private static final String MESSAGE_COMPONENT_DESCRIPTION = "module.crm.mydashboard.component.demands.description";
    private static final int COMPONENT_ID_MAXIMUM_SIZE = 50;

    @Inject
    private MyDashboardCRMComponent _component;

    @Inject
    private Instance<IMyDashboardComponent> _dashboardComponents;

    /**
     * The component is a CDI bean MyDashboard discovers among the dashboard components
     */
    @Test
    public void testComponentIsDiscoveredAsDashboardComponent( )
    {
        assertTrue( _dashboardComponents.stream( ).anyMatch( component -> COMPONENT_ID.equals( component.getComponentId( ) ) ),
                "the CRM demands component should be resolved as an IMyDashboardComponent" );
    }

    /**
     * The component id is the one the dashboard configurations reference, within the size MyDashboard accepts
     */
    @Test
    public void testComponentId( )
    {
        assertEquals( COMPONENT_ID, _component.getComponentId( ) );
        assertTrue( _component.getComponentId( ).length( ) <= COMPONENT_ID_MAXIMUM_SIZE, "the component id should fit the MyDashboard limit" );
    }

    /**
     * The description comes from the module bundle
     */
    @Test
    public void testComponentDescriptionIsLocalized( )
    {
        String strDescription = _component.getComponentDescription( Locale.FRENCH );

        assertTrue( StringUtils.isNotBlank( strDescription ), "the description should be localized" );
        assertNotEquals( MESSAGE_COMPONENT_DESCRIPTION, strDescription, "the description should not be the raw key" );
    }
}
