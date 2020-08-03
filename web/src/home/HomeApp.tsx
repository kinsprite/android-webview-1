import React from 'react';
import {
  NavLink,
} from 'react-router-dom';

import styles from './HomeApp.module.css';
import { sendMessageToNative } from '../nativeMessage';

function cameraOpen() {
  sendMessageToNative('camera_open');
}

function vibratorNotify() {
  sendMessageToNative('vibrator_notify');
}

function HomeApp(): JSX.Element {
  return (
    <div className={styles.App}>
      <header className={styles.AppHeader}>
        <div className={styles.BtnGroup}>
          <button type="button" onClick={cameraOpen}>Camera</button>
          <button type="button" onClick={vibratorNotify}>Vibrator</button>
        </div>
        <ul>
          <li>
            <NavLink to="/home" className={styles.AppLink}>Home</NavLink>
          </li>
          <li>
            <NavLink to="/app-example" className={styles.AppLink}>App Example</NavLink>
          </li>
        </ul>
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
