function nthu_hpclp_main(){var O='bootstrap',P='begin',Q='gwt.codesvr.nthu.hpclp.main=',R='gwt.codesvr=',S='nthu.hpclp.main',T='startup',U='DUMMY',V=0,W=1,X='iframe',Y='javascript:""',Z='position:absolute; width:0; height:0; border:none; left: -1000px;',$=' top: -1000px;',_='CSS1Compat',ab='<!doctype html>',bb='',cb='<html><head><\/head><body><\/body><\/html>',db='undefined',eb='DOMContentLoaded',fb=50,gb='Chrome',hb='eval("',ib='");',jb='script',kb='javascript',lb='moduleStartup',mb='moduleRequested',nb='nthu_hpclp_main',ob='Failed to load ',pb='head',qb='meta',rb='name',sb='nthu.hpclp.main::',tb='::',ub='gwt:property',vb='content',wb='=',xb='gwt:onPropertyErrorFn',yb='Bad handler "',zb='" for "gwt:onPropertyErrorFn"',Ab='gwt:onLoadErrorFn',Bb='" for "gwt:onLoadErrorFn"',Cb='#',Db='?',Eb='/',Fb='img',Gb='clear.cache.gif',Hb='baseUrl',Ib='nthu.hpclp.main.nocache.js',Jb='base',Kb='//',Lb='user.agent',Mb='webkit',Nb='safari',Ob='msie',Pb=10,Qb=11,Rb='ie10',Sb=9,Tb='ie9',Ub=8,Vb='ie8',Wb='gecko',Xb='gecko1_8',Yb=2,Zb=3,$b=4,_b='selectingPermutation',ac='nthu.hpclp.main.devmode.js',bc="GWT module 'nthu.hpclp.main' may need to be (re)compiled",cc=':',dc='.cache.js',ec='link',fc='rel',gc='stylesheet',hc='href',ic='loadExternalRefs',jc='css/overridecss.css',kc='css/animation.css',lc='css/material-icons.css',mc='css/materialize.min.css',nc='css/materialize.blue.css',oc='css/overridecss.blue.css',pc='end',qc='http:',rc='file:',sc='_gwt_dummy_',tc='__gwtDevModeHook:nthu.hpclp.main',uc='Ignoring non-whitelisted Dev Mode URL: ',vc=':moduleBase';var o=window;var p=document;r(O,P);function q(){var a=o.location.search;return a.indexOf(Q)!=-1||a.indexOf(R)!=-1}
function r(a,b){if(o.__gwtStatsEvent){o.__gwtStatsEvent({moduleName:S,sessionId:o.__gwtStatsSessionId,subSystem:T,evtGroup:a,millis:(new Date).getTime(),type:b})}}
nthu_hpclp_main.__sendStats=r;nthu_hpclp_main.__moduleName=S;nthu_hpclp_main.__errFn=null;nthu_hpclp_main.__moduleBase=U;nthu_hpclp_main.__softPermutationId=V;nthu_hpclp_main.__computePropValue=null;nthu_hpclp_main.__getPropMap=null;nthu_hpclp_main.__installRunAsyncCode=function(){};nthu_hpclp_main.__gwtStartLoadingFragment=function(){return null};nthu_hpclp_main.__gwt_isKnownPropertyValue=function(){return false};nthu_hpclp_main.__gwt_getMetaProperty=function(){return null};var s=null;var t=o.__gwt_activeModules=o.__gwt_activeModules||{};t[S]={moduleName:S};nthu_hpclp_main.__moduleStartupDone=function(e){var f=t[S].bindings;t[S].bindings=function(){var a=f?f():{};var b=e[nthu_hpclp_main.__softPermutationId];for(var c=V;c<b.length;c++){var d=b[c];a[d[V]]=d[W]}return a}};var u;function v(){w();return u}
function w(){if(u){return}var a=p.createElement(X);a.src=Y;a.id=S;a.style.cssText=Z+$;a.tabIndex=-1;p.body.appendChild(a);u=a.contentDocument;if(!u){u=a.contentWindow.document}u.open();var b=document.compatMode==_?ab:bb;u.write(b+cb);u.close()}
function A(k){function l(a){function b(){if(typeof p.readyState==db){return typeof p.body!=db&&p.body!=null}return /loaded|complete/.test(p.readyState)}
var c=b();if(c){a();return}function d(){if(!c){c=true;a();if(p.removeEventListener){p.removeEventListener(eb,d,false)}if(e){clearInterval(e)}}}
if(p.addEventListener){p.addEventListener(eb,d,false)}var e=setInterval(function(){if(b()){d()}},fb)}
function m(c){function d(a,b){a.removeChild(b)}
var e=v();var f=e.body;var g;if(navigator.userAgent.indexOf(gb)>-1&&window.JSON){var h=e.createDocumentFragment();h.appendChild(e.createTextNode(hb));for(var i=V;i<c.length;i++){var j=window.JSON.stringify(c[i]);h.appendChild(e.createTextNode(j.substring(W,j.length-W)))}h.appendChild(e.createTextNode(ib));g=e.createElement(jb);g.language=kb;g.appendChild(h);f.appendChild(g);d(f,g)}else{for(var i=V;i<c.length;i++){g=e.createElement(jb);g.language=kb;g.text=c[i];f.appendChild(g);d(f,g)}}}
nthu_hpclp_main.onScriptDownloaded=function(a){l(function(){m(a)})};r(lb,mb);var n=p.createElement(jb);n.src=k;if(nthu_hpclp_main.__errFn){n.onerror=function(){nthu_hpclp_main.__errFn(nb,new Error(ob+code))}}p.getElementsByTagName(pb)[V].appendChild(n)}
nthu_hpclp_main.__startLoadingFragment=function(a){return D(a)};nthu_hpclp_main.__installRunAsyncCode=function(a){var b=v();var c=b.body;var d=b.createElement(jb);d.language=kb;d.text=a;c.appendChild(d);c.removeChild(d)};function B(){var c={};var d;var e;var f=p.getElementsByTagName(qb);for(var g=V,h=f.length;g<h;++g){var i=f[g],j=i.getAttribute(rb),k;if(j){j=j.replace(sb,bb);if(j.indexOf(tb)>=V){continue}if(j==ub){k=i.getAttribute(vb);if(k){var l,m=k.indexOf(wb);if(m>=V){j=k.substring(V,m);l=k.substring(m+W)}else{j=k;l=bb}c[j]=l}}else if(j==xb){k=i.getAttribute(vb);if(k){try{d=eval(k)}catch(a){alert(yb+k+zb)}}}else if(j==Ab){k=i.getAttribute(vb);if(k){try{e=eval(k)}catch(a){alert(yb+k+Bb)}}}}}__gwt_getMetaProperty=function(a){var b=c[a];return b==null?null:b};s=d;nthu_hpclp_main.__errFn=e}
function C(){function e(a){var b=a.lastIndexOf(Cb);if(b==-1){b=a.length}var c=a.indexOf(Db);if(c==-1){c=a.length}var d=a.lastIndexOf(Eb,Math.min(c,b));return d>=V?a.substring(V,d+W):bb}
function f(a){if(a.match(/^\w+:\/\//)){}else{var b=p.createElement(Fb);b.src=a+Gb;a=e(b.src)}return a}
function g(){var a=__gwt_getMetaProperty(Hb);if(a!=null){return a}return bb}
function h(){var a=p.getElementsByTagName(jb);for(var b=V;b<a.length;++b){if(a[b].src.indexOf(Ib)!=-1){return e(a[b].src)}}return bb}
function i(){var a=p.getElementsByTagName(Jb);if(a.length>V){return a[a.length-W].href}return bb}
function j(){var a=p.location;return a.href==a.protocol+Kb+a.host+a.pathname+a.search+a.hash}
var k=g();if(k==bb){k=h()}if(k==bb){k=i()}if(k==bb&&j()){k=e(p.location.href)}k=f(k);return k}
function D(a){if(a.match(/^\//)){return a}if(a.match(/^[a-zA-Z]+:\/\//)){return a}return nthu_hpclp_main.__moduleBase+a}
function F(){var f=[];var g=V;var h=[];var i=[];function j(a){var b=i[a](),c=h[a];if(b in c){return b}var d=[];for(var e in c){d[c[e]]=e}if(s){s(a,d,b)}throw null}
i[Lb]=function(){var a=navigator.userAgent.toLowerCase();var b=p.documentMode;if(function(){return a.indexOf(Mb)!=-1}())return Nb;if(function(){return a.indexOf(Ob)!=-1&&(b>=Pb&&b<Qb)}())return Rb;if(function(){return a.indexOf(Ob)!=-1&&(b>=Sb&&b<Qb)}())return Tb;if(function(){return a.indexOf(Ob)!=-1&&(b>=Ub&&b<Qb)}())return Vb;if(function(){return a.indexOf(Wb)!=-1||b>=Qb}())return Xb;return bb};h[Lb]={gecko1_8:V,ie10:W,ie8:Yb,ie9:Zb,safari:$b};__gwt_isKnownPropertyValue=function(a,b){return b in h[a]};nthu_hpclp_main.__getPropMap=function(){var a={};for(var b in h){if(h.hasOwnProperty(b)){a[b]=j(b)}}return a};nthu_hpclp_main.__computePropValue=j;o.__gwt_activeModules[S].bindings=nthu_hpclp_main.__getPropMap;r(O,_b);if(q()){return D(ac)}var k;try{alert(bc);return;var l=k.indexOf(cc);if(l!=-1){g=parseInt(k.substring(l+W),Pb);k=k.substring(V,l)}}catch(a){}nthu_hpclp_main.__softPermutationId=g;return D(k+dc)}
function G(){if(!o.__gwt_stylesLoaded){o.__gwt_stylesLoaded={}}function c(a){if(!__gwt_stylesLoaded[a]){var b=p.createElement(ec);b.setAttribute(fc,gc);b.setAttribute(hc,D(a));p.getElementsByTagName(pb)[V].appendChild(b);__gwt_stylesLoaded[a]=true}}
r(ic,P);c(jc);c(kc);c(lc);c(mc);c(nc);c(oc);r(ic,pc)}
B();nthu_hpclp_main.__moduleBase=C();t[S].moduleBase=nthu_hpclp_main.__moduleBase;var H=F();if(o){var I=!!(o.location.protocol==qc||o.location.protocol==rc);o.__gwt_activeModules[S].canRedirect=I;function J(){var b=sc;try{o.sessionStorage.setItem(b,b);o.sessionStorage.removeItem(b);return true}catch(a){return false}}
if(I&&J()){var K=tc;var L=o.sessionStorage[K];if(!/^http:\/\/(localhost|127\.0\.0\.1)(:\d+)?\/.*$/.test(L)){if(L&&(window.console&&console.log)){console.log(uc+L)}L=bb}if(L&&!o[K]){o[K]=true;o[K+vc]=C();var M=p.createElement(jb);M.src=L;var N=p.getElementsByTagName(pb)[V];N.insertBefore(M,N.firstElementChild||N.children[V]);return false}}}G();r(O,pc);A(H);return true}
nthu_hpclp_main.succeeded=nthu_hpclp_main();