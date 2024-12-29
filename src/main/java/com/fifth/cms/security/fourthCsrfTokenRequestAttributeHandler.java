package com.fifth.cms.security;

import java.util.function.Supplier;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.util.Assert;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class fourthCsrfTokenRequestAttributeHandler implements CsrfTokenRequestHandler {

	private String csrfRequestAttributeName = "_csrf";
	
	/**
	 * The {@link CsrfToken} is available as a request attribute named
	 * {@code CsrfToken.class.getName()}. By default, an additional request attribute that
	 * is the same as {@link CsrfToken#getParameterName()} is set. This attribute allows
	 * overriding the additional attribute.
	 * @param csrfRequestAttributeName the name of an additional request attribute with
	 * the value of the CsrfToken. Default is {@link CsrfToken#getParameterName()}
	 */
	public final void setCsrfRequestAttributeName(String csrfRequestAttributeName) {
		this.csrfRequestAttributeName = csrfRequestAttributeName;
	}
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			Supplier<CsrfToken> deferredCsrfToken) {
		Assert.notNull(request, "request cannot be null");
		Assert.notNull(response, "response cannot be null");
		Assert.notNull(deferredCsrfToken, "deferredCsrfToken cannot be null");

		System.out.println("CsrfTokenRequestAttributeHandler 접근");
		
		request.setAttribute(HttpServletResponse.class.getName(), response);
		CsrfToken csrfToken = new SupplierCsrfToken(deferredCsrfToken);
		request.setAttribute(CsrfToken.class.getName(), csrfToken);
		String csrfAttrName = (this.csrfRequestAttributeName != null) ? this.csrfRequestAttributeName
				: csrfToken.getParameterName();
		request.setAttribute(csrfAttrName, csrfToken);
		
		System.out.println("CsrfTokenRequestAttributeHandler 정보 : " + csrfAttrName + " : " + csrfToken.getHeaderName() + " : " + csrfToken.getParameterName() + " : " + csrfToken.getToken());
		
	}
	
	private static final class SupplierCsrfToken implements CsrfToken {

		private final Supplier<CsrfToken> csrfTokenSupplier;

		private SupplierCsrfToken(Supplier<CsrfToken> csrfTokenSupplier) {
			this.csrfTokenSupplier = csrfTokenSupplier;
		}

		@Override
		public String getHeaderName() {
			return getDelegate().getHeaderName();
		}

		@Override
		public String getParameterName() {
			return getDelegate().getParameterName();
		}

		@Override
		public String getToken() {
			return getDelegate().getToken();
		}

		private CsrfToken getDelegate() {
			CsrfToken delegate = this.csrfTokenSupplier.get();
			if (delegate == null) {
				throw new IllegalStateException("csrfTokenSupplier returned null delegate");
			}
			return delegate;
		}

	}

}
