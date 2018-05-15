<%--
  Created by IntelliJ IDEA.
  User: mehanna
  Date: 20/07/17
  Time: 18:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>UNWHEEZE CRUD API</title>
</head>
<body>
<center><h1>UNWHEEZE</h1></center>

<center><p>Click <a href="indexDoc.html">here</a> in order to acces the API documentation</p></center>
<style type="text/css">
  .tg  {border-collapse:collapse;border-spacing:0;}
  .tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:black;}
  .tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:black;}
  .tg .tg-e3zv{font-weight:bold}
</style>

<center><h2>UNWHEEZE APK LINKS</h2>
  <p>To access the app documentation, click <a href="AppDoc/index.html">here</a></p><br/>
  <table class="tg">
  <tr>
    <th class="tg-e3zv">Version</th>
    <th class="tg-e3zv">Link</th>
    <th class="tg-e3zv">Comments</th>
  </tr>
    <tr>
      <td class="tg-031e">Bêta 0.1.3</td>
      <td class="tg-031e"><a href="http://www.droidbin.com/p1cdhqjjb7j455kgqemni7bs63" target="_blank">UnwheezeBeta0.1.3.apk</a></td>
      <td class="tg-031e">Ajout d'une icone sur le app drawer (ronde et carré). Réglage de quelques problèmes de design.</td>
    </tr>
    <tr>
      <td class="tg-031e">Bêta 0.1.2</td>
      <td class="tg-031e"><a href="http://www.droidbin.com/p1cdfmmb4le767g1ga91jo3ss03" target="_blank">UnwheezeBeta0.1.2.apk</a></td>
      <td class="tg-031e">Ajout d'un delai de chargement en cas de gros travail pour récupérer les données. Un problème avec la 4G que nous tentons de resoudre est présent des toutes les versions.</td>
    </tr>
    <tr>
      <td class="tg-031e">Bêta 0.1.1</td>
      <td class="tg-031e"><a href="http://www.droidbin.com/p1cdcmhncisf517jaisi9qf1dbm3" target="_blank">UnwheezeBeta0.1.1.apk</a></td>
      <td class="tg-031e">Fixing some bugs with the periodic measure. Also changed the labels.</td>
    </tr>
    <tr>
      <td class="tg-031e">Bêta 0.1.0</td>
      <td class="tg-031e"><a href="http://www.droidbin.com/p1cdbc78pvb4f1sde1vbp59jpc23" target="_blank">UnwheezeBeta0.1.0.apk</a></td>
      <td class="tg-031e">New feature to measure periodically (fixed 30 s for now, may be able to chose the period in the future). Still in beta testing, expect many bugs. It's still not working when the screen turn off but it's a good start.
      Also fixed some bugs along the line.</td>
    </tr>
    <tr>
      <td class="tg-031e">Bêta 0.0.5</td>
      <td class="tg-031e"><a href="http://www.droidbin.com/p1cd8a9kk01h5gc7j8gl1b7tufi3" target="_blank">UnwheezeBeta0.0.5.apk</a></td>
      <td class="tg-031e">Added extensive error handling.</td>
    </tr>
  <tr>
    <td class="tg-031e">Bêta 0.0.4</td>
    <td class="tg-031e"><a href="http://www.droidbin.com/p1ccvlrkeo1oi91h821apt10rqbln3" target="_blank">UnwheezeBeta0.0.4.apk</a></td>
    <td class="tg-031e">Fixed the failing to charge at the app opening. Now cleaning database at every app reopening to check for deleted points. Refresh button do the same.</td>
  </tr>
  <tr>
    <td class="tg-031e">Bêta 0.0.3</td>
    <td class="tg-031e"><a href="http://www.droidbin.com/p1cctjb3o01duidc515js1eqo1sgr3" target="_blank">UnwheezeBeta0.0.3.apk</a></td>
    <td class="tg-031e">Changed measure request architecture to handle null location using the android location listener</td>
  </tr>
  <tr>
    <td class="tg-031e">Bêta 0.0.2</td>
    <td class="tg-031e"><a href="http://www.droidbin.com/p1cctf2mk2146plv0nt71ehot2g3" target="_blank">UnwheezeBeta0.0.2.apk</a></td>
    <td class="tg-031e">Handled crashing when app switch from portrait to landscape</td>
  </tr>
  <tr>
    <td class="tg-031e">Bêta 0.0.1</td>
    <td class="tg-031e"><a href="http://www.droidbin.com/p1ccoqn899hv4123p1e351h641vdh3" target="_blank">UnwheezeBetav0.0.1.apk</a></td>
    <td class="tg-031e">Added some UI features and corrected small connectivity bugs</td>
  </tr>
  <tr>
    <td class="tg-031e">Bêta 0.0.0</td>
    <td class="tg-031e"><a href="Unwheeze.apk">UnwheezeBeta0.0.0.apk</a> </td>
    <td class="tg-031e">First working version</td>
  </tr>

</table>
</center>
</body>
</html>