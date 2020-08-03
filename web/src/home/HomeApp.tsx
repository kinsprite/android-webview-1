import React from 'react';
import {
  NavLink,
} from 'react-router-dom';

import LoginAsTester from './LoginAsTester';

import logo from './logo.svg';
import styles from './HomeApp.module.css';

function HomeApp(): JSX.Element {
  return (
    <div className={styles.App}>
      <header className={styles.AppHeader}>
        <LoginAsTester />
        <img src={logo} className={styles.AppLogo} alt="logo" />
        <p>
          <code>React Micro Frontends @ 2020</code>
        </p>
        <ul>
          <li>
            <NavLink to="/home" className={styles.AppLink}>Home</NavLink>
          </li>
          <li>
            <NavLink to="/app-example" className={styles.AppLink}>App Example</NavLink>
          </li>
        </ul>
        <div data-spa-render="spaDynamicView2" />
      </header>
      <footer className={styles.AppFooter}>
        <a
          className={styles.FooterLink}
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
        <a
          className={styles.FooterLink}
          href="https://qinzhiqiang.cn"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn Micro Frontends
        </a>
      </footer>
    </div>
  );
}

export default HomeApp;
