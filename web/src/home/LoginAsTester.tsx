import React, { useEffect, useState } from 'react';

import styles from './LoginAsTester.module.css';

function LoginAsTester(): JSX.Element {
  const [once] = useState(1);
  const [loading, SetLoading] = useState(true);
  const [isTester, setIsTester] = useState(false);

  useEffect(() => {
    fetch('/api/user/is-tester', { credentials: 'same-origin' }).then((response) => {
      SetLoading(false);

      if (response.ok) {
        response.json().then((value: boolean) => {
          setIsTester(value);
        });
      }
    }, () => {
      SetLoading(false);
    });
  }, [once]);

  function onBtnClicked() {
    const value = !isTester;

    fetch('/api/user/login-as-tester', {
      body: JSON.stringify(value),
      credentials: 'same-origin',
      method: 'POST',
      headers: {
        'content-type': 'application/json',
      },
    }).then((response) => {
      if (response.ok) {
        setIsTester(value);
        window.location.reload();
      }
    });
  }

  if (loading) {
    return <></>;
  }

  return (
    <div className={styles.container}>
      <button
        type="button"
        className={styles.btn}
        onClick={onBtnClicked}
        title={isTester ? 'Return to the normal page' : 'Login as a tester and reload'}
      >
        {isTester ? 'Logout' : 'Login' }
      </button>
    </div>
  );
}

export default LoginAsTester;
