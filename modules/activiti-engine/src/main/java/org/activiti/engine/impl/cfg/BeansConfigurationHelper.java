/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.engine.impl.cfg;

import java.io.InputStream;

import org.activiti.engine.ProcessEngineConfiguration;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;


/**
 * @author Tom Baeyens
 * @desc  此类主要是
 * Activiti通过此类 巧妙的将流程配置文件中定义的bean全部交给Spring容器进行管理(
 * 包括类的配置 ,加载和获取操作 等等)
 */
public class BeansConfigurationHelper {
  /*

   */
  public static ProcessEngineConfiguration parseProcessEngineConfiguration(Resource springResource, String beanName) {
    //实例化Spring框架中的DefaultListableBeanFactory 类
    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
    //设置验证模式为XSD 也支持DTD
    xmlBeanDefinitionReader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_XSD);
    //加载读取到的springReource资源交给SPirng解析
    xmlBeanDefinitionReader.loadBeanDefinitions(springResource);
    /*
     通过beanFactory对象获取 ProcessEngineConfigurationImpl 对象
     这是最典型的Spring获取bean实例对象的操作方式
     实例化  流程引擎配置类 的同时 会初始化其父类 ProcessEngineConfigurationImpl类中的各种属性值
     */
    ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) beanFactory.getBean(beanName);
    //将beanFactory对象使用SpringBenaFactoryProxyMap 包装
    processEngineConfiguration.setBeans(new SpringBeanFactoryProxyMap(beanFactory));
    return processEngineConfiguration;
  }
  /*

   */
  public static ProcessEngineConfiguration parseProcessEngineConfigurationFromInputStream(InputStream inputStream, String beanName) {
    //  将inputstream 转化为Spring的resource对象
    Resource springResource = new InputStreamResource(inputStream);
    //实例化流程配置文件中的bean
    return parseProcessEngineConfiguration(springResource, beanName);
  }

  public static ProcessEngineConfiguration parseProcessEngineConfigurationFromResource(String resource, String beanName) {
    Resource springResource = new ClassPathResource(resource);
    return parseProcessEngineConfiguration(springResource, beanName);
  }

}
