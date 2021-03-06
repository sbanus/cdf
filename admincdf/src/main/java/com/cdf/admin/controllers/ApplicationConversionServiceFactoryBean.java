package com.cdf.admin.controllers;

import com.cdf.admin.entity.FavourModel;
import com.cdf.admin.entity.UserModel;
import com.cdf.admin.service.FavourModelService;
import com.cdf.admin.service.UserModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}

	@Autowired
    FavourModelService favourModelService;

	@Autowired
    UserModelService userModelService;

	public Converter<FavourModel, String> getFavourModelToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.cdf.admin.entity.FavourModel, java.lang.String>() {
            public String convert(FavourModel favourModel) {
                return new StringBuilder().append(favourModel.getDescription()).toString();
            }
        };
    }

	public Converter<Long, FavourModel> getIdToFavourModelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.cdf.admin.entity.FavourModel>() {
            public com.cdf.admin.entity.FavourModel convert(java.lang.Long id) {
                return favourModelService.findFavourModel(id);
            }
        };
    }

	public Converter<String, FavourModel> getStringToFavourModelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.cdf.admin.entity.FavourModel>() {
            public com.cdf.admin.entity.FavourModel convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), FavourModel.class);
            }
        };
    }

	public Converter<UserModel, String> getUserModelToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.cdf.admin.entity.UserModel, java.lang.String>() {
            public String convert(UserModel userModel) {
                return new StringBuilder().append(userModel.getName()).append(' ').append(userModel.getPassword()).append(' ').append(userModel.getFirstName()).append(' ').append(userModel.getLastName()).toString();
            }
        };
    }

	public Converter<Long, UserModel> getIdToUserModelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.cdf.admin.entity.UserModel>() {
            public com.cdf.admin.entity.UserModel convert(java.lang.Long id) {
                return userModelService.findUserModel(id);
            }
        };
    }

	public Converter<String, UserModel> getStringToUserModelConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.cdf.admin.entity.UserModel>() {
            public com.cdf.admin.entity.UserModel convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), UserModel.class);
            }
        };
    }

	public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getFavourModelToStringConverter());
        registry.addConverter(getIdToFavourModelConverter());
        registry.addConverter(getStringToFavourModelConverter());
        registry.addConverter(getUserModelToStringConverter());
        registry.addConverter(getIdToUserModelConverter());
        registry.addConverter(getStringToUserModelConverter());
    }

	public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
}
